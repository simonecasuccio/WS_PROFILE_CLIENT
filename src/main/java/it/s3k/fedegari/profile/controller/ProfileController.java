package it.s3k.fedegari.profile.controller;

import it.s3k.fedegari.profile.domain.ProfileProject;
import it.s3k.fedegari.profile.domain.ProfileTask;
import it.s3k.fedegari.profile.exception.EntityNotFoundException;
import it.s3k.fedegari.profile.exception.ValidationException;
import it.s3k.fedegari.profile.model.*;
import it.s3k.fedegari.profile.service.ProfileService;
import it.s3k.fedegari.profile.service.ProjectService;
import it.s3k.fedegari.profile.service.ProfileFieldMappingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;
    private final ProfileFieldMappingService mappingService;
    private final ProjectService projectService;

    public ProfileController(ProfileService profileService, ProfileFieldMappingService mappingService, ProjectService projectService) {
        this.profileService = profileService;
        this.mappingService = mappingService;
        this.projectService = projectService;
    }

    /**
     * Unified endpoint for creating or updating projects and tasks
     */
    @PostMapping("/sync")
    public ResponseEntity<ProjectResponse> syncProject(@RequestBody ProjectRequest request) {
        logger.info("Syncing project with tasks from Asana. CorrelationId: {}", request.getCorrelationId());

        ProjectResponse response = new ProjectResponse();
        response.setCorrelationId(request.getCorrelationId());

        try {
            // Validate request
            mappingService.validateProjectRequest(request);

            // Create or update project with tasks
            CreationResult result = projectService.createOrUpdateProjectWithTasks(request);

            // Map result to response
            if ("SUCCESS".equals(result.getResultCode())) {
                response.setResultCode("SUCCESS");

                // Add projects data
                List<PlmProjectData> projectDataList = result.getProjectResults().stream()
                        .map(projectResult -> {
                            PlmProjectData projectData = new PlmProjectData();
                            projectData.setAsanaProjectId(projectResult.getAsanaProjectId());
                            projectData.setPlmProjectId(projectResult.getProfileProjectId());
                            return projectData;
                        })
                        .collect(Collectors.toList());
                response.setPlmProjectData(projectDataList);

                // Add tasks data if any were processed
                if (result.getTaskResults() != null && !result.getTaskResults().isEmpty()) {
                    List<PlmTaskData> taskDataList = result.getTaskResults().stream()
                            .map(taskResult -> {
                                PlmTaskData taskData = new PlmTaskData();
                                taskData.setAsanaTaskId(taskResult.getAsanaTaskId());
                                taskData.setPlmTaskId(taskResult.getProfileTaskId());
                                return taskData;
                            })
                            .collect(Collectors.toList());
                    response.setPlmTaskData(taskDataList);
                }

                logger.info("Projects and tasks synced successfully. Number of projects: {}", projectDataList.size());
                return ResponseEntity.ok(response);

            } else {
                response.setResultCode("ERROR");
                response.setResultDescription(result.getResultDescription());
                logger.warn("Error in project sync flow: {}", result.getResultDescription());
                return ResponseEntity.badRequest().body(response);
            }

        } catch (ValidationException e) {
            logger.warn("Validation error for correlationId {}: {}", request.getCorrelationId(), e.getMessage());
            response.setResultCode("VALIDATION_ERROR");
            response.setResultDescription(e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            logger.error("Internal error syncing project for correlationId " + request.getCorrelationId(), e);
            response.setResultCode("INTERNAL_ERROR");
            response.setResultDescription("Internal server error");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
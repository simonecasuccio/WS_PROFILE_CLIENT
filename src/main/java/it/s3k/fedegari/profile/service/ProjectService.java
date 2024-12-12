package it.s3k.fedegari.profile.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.s3k.fedegari.profile.model.CreationResult;
import it.s3k.fedegari.profile.model.ProjectCreationResult;
import it.s3k.fedegari.profile.model.ProjectRequest;
import it.s3k.fedegari.profile.model.ProjectRequest.AsanaProject;
import it.s3k.fedegari.profile.model.ProjectRequest.AsanaTask;
import it.s3k.fedegari.profile.model.TaskCreationResult;
import it.s3k.fedegari.profile.domain.ProfileProject;
import it.s3k.fedegari.profile.domain.ProfileTask;
import it.s3k.fedegari.profile.exception.EntityNotFoundException;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private final ProfileFieldMappingService mappingService;
    private final ProfileService profileService;

    public ProjectService(ProfileFieldMappingService mappingService, ProfileService profileService) {
        this.mappingService = mappingService;
        this.profileService = profileService;
    }

    public CreationResult createOrUpdateProjectWithTasks(ProjectRequest request) {
        CreationResult result = new CreationResult();
        List<ProjectCreationResult> allProjectResults = new ArrayList<>();
        List<TaskCreationResult> allTaskResults = new ArrayList<>();
        Map<String, String> asanaToPlmProjectIds = new HashMap<>();

        try {
            // 1. Process all projects (create or update)
            if (request.getAsanaProjects() != null && !request.getAsanaProjects().isEmpty()) {
                for (AsanaProject project : request.getAsanaProjects()) {
                    JSONObject projectFields = mappingService.mapToProfileProjectFields(project);
                    String profileProjectId = null;
                    String asanaProjectId = null;
                    
                    try {
                        // Try to get existing project
                        asanaProjectId = project.getAsanaProjectId();
                        ProfileProject profileProject = profileService.getProjectByAsanaId(asanaProjectId);
                        // Project exists, update it
                        profileProjectId = profileProject.getProfileProjectId();
                        profileService.updateProject(profileProjectId, projectFields);
                        logger.info("Updated existing project with ID: {}", profileProjectId);
                    } catch (EntityNotFoundException e) {
                        // Project doesn't exist, create it
                        profileProjectId = profileService.createProject(projectFields);
                        logger.info("Created new project with ID: {}", profileProjectId);
                    }

                    asanaToPlmProjectIds.put(project.getAsanaProjectId(), profileProjectId);

                    ProjectCreationResult projectResult = new ProjectCreationResult();
                    projectResult.setProfileProjectId(profileProjectId);
                    projectResult.setAsanaProjectId(project.getAsanaProjectId());
                    allProjectResults.add(projectResult);
                }
            }

            // 2. Process all tasks (create or update) and their links to projects
            if (request.getAsanaTasks() != null && !request.getAsanaTasks().isEmpty()) {
                for (AsanaTask task : request.getAsanaTasks()) {
                    JSONObject taskFields = mappingService.mapToProfileTaskFields(task);
                    String profileTaskId = null;
                    String asanaTaskId = null;

                    try {
                        // Try to get existing task
                        asanaTaskId = task.getAsanaTaskId();
                        ProfileTask profileTask = profileService.getTaskByAsanaId(asanaTaskId);
                        // Task exists, update it
                        profileTaskId = profileTask.getProfileTaskId();
                        profileService.updateTask(profileTaskId, taskFields);
                        logger.info("Updated existing task with ID: {}", asanaTaskId);
                    } catch (EntityNotFoundException e) {
                        // Task doesn't exist, create it
                        profileTaskId = profileService.createTask(taskFields);
                        logger.info("Created new task with ID: {}", profileTaskId);
                    }

                    // Create links between task and its related projects
                    if (task.getRelatedProjects() != null) {
                        for (ProjectRequest.RelatedProject relatedProject : task.getRelatedProjects()) {
                            if (relatedProject.getAsanaProjectId() != null && !relatedProject.getAsanaProjectId().isEmpty()) {
                                String plmProjectId = asanaToPlmProjectIds.get(relatedProject.getAsanaProjectId());
                                try {
                                    profileService.createProjectTaskLink(plmProjectId, profileTaskId);
                                    logger.info("Created link between project {} and task {}", plmProjectId, profileTaskId);
                                } catch (Exception e) {
                                    // Link might already exist, log and continue
                                    logger.debug("Link between project {} and task {} might already exist", plmProjectId, profileTaskId);
                                }
                            }
                        }
                    }

                    TaskCreationResult taskResult = new TaskCreationResult();
                    taskResult.setProfileTaskId(profileTaskId);
                    taskResult.setAsanaTaskId(asanaTaskId);
                    allTaskResults.add(taskResult);
                }
            }

            result.setProjectResults(allProjectResults);
            result.setTaskResults(allTaskResults);
            result.setResultCode("SUCCESS");

        } catch (Exception e) {
            logger.error("Error processing projects with tasks", e);
            result.setResultCode("ERROR");
            result.setResultDescription(e.getMessage());
        }

        return result;
    }

    // Keep the old method for backward compatibility if needed
    public CreationResult createProjectWithTasks(ProjectRequest request) {
        return createOrUpdateProjectWithTasks(request);
    }
}
package it.s3k.fedegari.profile.service;

import it.s3k.fedegari.profile.domain.ProfileTask;
import it.s3k.fedegari.profile.exception.ValidationException;
import it.s3k.fedegari.profile.model.ProjectRequest;
import it.s3k.fedegari.profile.model.ProjectRequest.AsanaProject;
import it.s3k.fedegari.profile.model.ProjectRequest.AsanaTask;
import it.s3k.fedegari.profile.model.ProjectRequest.RelatedProject;
import it.s3k.fedegari.profile.model.TaskDTO;
import it.s3k.fedegari.profile.model.TaskDatesUpdate;
import it.s3k.fedegari.profile.model.TaskRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
@Slf4j
public class ProfileFieldMappingService {

    public void validateProjectRequest(ProjectRequest request) {
        // TODO
    }

    public void validateTaskRequest(TaskRequest request) {
        // TODO
    }

    public void validateTaskDates(TaskDatesUpdate request) {
        if (request.getActualStartDate() != null && request.getActualFinishDate() != null) {
            if (request.getActualFinishDate().isBefore(request.getActualStartDate())) {
                throw new ValidationException("Task actual finish date cannot be before actual start date");
            }
        }
    }

    /**
     * Utility method to check if a string is not null and not empty
     */
    private boolean isNotNullOrEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Maps Asana project fields to corresponding Profile fields based on the data
     * mapping
     * 
     * @param project The AsanaProject data to map
     * @return JSONObject containing mapped fields for Profile
     */
    public JSONObject mapToProfileProjectFields(AsanaProject project) {
        JSONObject fields = new JSONObject();

        // Map required fields
        fields.put("/6/51", project.getAsanaProjectId()); // FEDAsanaGID
        fields.put("/6/52", formatDateTime(project.getCreatedAt())); // FEDAsanaCreatedAt
        fields.put("/6/2", project.getProjectName()); // pdmProjectName
        fields.put("/6/100", extractMachineSerialNumber(project.getProjectName())); // FEDMachine - first 8 chars
        fields.put("/6/99", project.getMachineModel()); // FEDProjectType

        // Map optional fields
        if (isNotNullOrEmpty(project.getStatusColor())) {
            fields.put("/6/75", project.getStatusColor()); // SI
        }

        if (isNotNullOrEmpty(project.getProjectEngineer())) {
            fields.put("/6/8", project.getProjectEngineer()); // FEDPE
        }

        if (isNotNullOrEmpty(project.getProjectNotes())) {
            fields.put("/6/23", project.getProjectNotes()); // pdmProjectDescription
        }

        if (isNotNullOrEmpty(project.getOwnerName())) {
            fields.put("/6/101", project.getOwnerName()); // FEDAsanaOwner
        }

        if (project.getStartOn() != null) {
            fields.put("/6/18", project.getStartOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // pdmProjectF00
        }

        if (project.getDueDate() != null) {
            fields.put("/6/19", project.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // pdmProjectF01
        }

        if (project.getModifiedAt() != null) {
            fields.put("/6/53", formatDateTime(project.getModifiedAt())); // FEDAsanaModifiedAt
        }

        if (isNotNullOrEmpty(project.getPermalinkUrl())) {
            fields.put("/6/59", project.getPermalinkUrl()); // FEDAsanaURL
        }

        // manca mapping lato Asana
        /*if (isNotNullOrEmpty(project.getModifiedBy())) {
            fields.put("/6/102", project.getModifiedBy()); // FEDAsanaModifiedBy
        }*/

        return fields;
    }

    /**
     * Extracts the machine serial number from the project name (first 8 characters)
     */
    private String extractMachineSerialNumber(String projectName) {
        if (isNotNullOrEmpty(projectName) && projectName.length() >= 8) {
            return projectName.substring(0, 8);
        }
        return null;
    }

    /**
     * Formats ZonedDateTime to Profile expected date format
     */
    private String formatDate(ZonedDateTime date) {
        if (date == null)
            return null;
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Formats ZonedDateTime to Profile expected datetime format
     */
    private String formatDateTime(ZonedDateTime dateTime) {
        if (dateTime == null)
            return null;
        return dateTime.format(DateTimeFormatter.ISO_INSTANT);
    }

    /**
     * Maps Asana task fields to corresponding Profile fields based on the data
     * mapping
     * 
     * @param task The Asana task data to map
     * @return JSONObject containing mapped fields for Profile task
     */
    public JSONObject mapToProfileTaskFields(AsanaTask task) {
        JSONObject fields = new JSONObject();

        // Map required fields
        fields.put("/31/100", task.getAsanaTaskId()); // FEDAsanaGID
        fields.put("/31/1", task.getTaskName()); // pdmTaskName

        // Map optional fields
        if (isNotNullOrEmpty(task.getAssigneeName())) {
            fields.put("/31/12", task.getAssigneeName()); // pdmTaskUser
        }

        if (isNotNullOrEmpty(task.getTaskNotes())) {
            fields.put("/31/2", task.getTaskNotes()); // pdmTaskDescription
        }

        if (task.getCreatedAt() != null) {
            fields.put("/31/101", formatDateTime(task.getCreatedAt())); // FEDAsanaCreatedAt
        }

        if (task.getModifiedAt() != null) {
            fields.put("/31/102", formatDateTime(task.getModifiedAt())); // FEDAsanaModifiedAt
        }

        if (task.getStartTimestamp() != null) {
            fields.put("/31/8", formatDateTime(task.getStartTimestamp())); // pdmTaskPlannedStartDate
        }

        if (task.getDueDate() != null) {
            fields.put("/31/9", task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // pdmTaskPlannedEndDate
        }

        if (task.getCompletedAt() != null) {
            fields.put("/31/11", formatDateTime(task.getCompletedAt())); // pdmTaskActualEndDate
        }

        if (isNotNullOrEmpty(task.getProjectEngineer())) {
            fields.put("/31/59", task.getProjectEngineer()); // FEDPE
        }

        if (isNotNullOrEmpty(task.getPermalinkUrl())) {
            fields.put("/31/58", task.getPermalinkUrl()); // FEDAsanaURL
        }

        // manca mapping lato Asana
        /*if (isNotNullOrEmpty(task.getModifiedBy())) {
            fields.put("/31/60", task.getModifiedBy()); // FEDAsanaModifiedBy
        }*/

        // Task status mapping if needed
        if (task.getTaskCompleted() != null && task.getTaskCompleted()) {
            fields.put("/31/25", "Task_ended"); // Status code for completed tasks
        }

        return fields;
    }

    /**
     * Creates a link between a task and its related project in Profile
     * 
     * @param task The task containing related project information
     * @return JSONObject containing the link information
     */
    public JSONObject createTaskProjectLink(AsanaTask task, RelatedProject relatedProject) {
        JSONObject link = new JSONObject();
        link.put("parentKey", String.format("urn:key:Project:%s", relatedProject.getAsanaProjectId()));
        link.put("childKey", String.format("urn:key:Task:%s", task.getAsanaTaskId()));
        link.put("classification", Arrays.asList("/Relationship/"));
        link.put("fieldValues", new JSONObject());
        return link;
    }

    /*public TaskDTO mapFromProfileTask(ProfileTask task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setAssignee(task.getAssignee());
        dto.setProjectId(task.getProjectId());
        dto.setPlannedStartDate(task.getPlannedStartDate());
        dto.setPlannedFinishDate(task.getPlannedFinishDate());
        dto.setActualStartDate(task.getActualStartDate());
        dto.setActualFinishDate(task.getActualFinishDate());
        dto.setCustomFields(task.getCustomFields());
        return dto;
    }*/
}
package it.s3k.fedegari.profile.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectRequest {
    @JsonProperty("correlationId")
    private String correlationId;
    
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime timestamp;
    
    @JsonProperty("asanaProjects")
    private List<AsanaProject> asanaProjects;

    @JsonProperty("asanaTasks")
    private List<AsanaTask> asanaTasks;

    @Data
    public static class AsanaProject {
        @JsonProperty("asanaProjectId")
        private String asanaProjectId;

        @JsonProperty("createdAt") 
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private ZonedDateTime createdAt;

        @JsonProperty("statusColor")
        private String statusColor;

        @JsonProperty("statusUpdate")
        private String statusUpdate;

        @JsonProperty("projectEngineer")
        private String projectEngineer;

        @JsonProperty("machineModel")
        private String machineModel;

        @JsonProperty("dateQG5")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private ZonedDateTime dateQG5;

        @JsonProperty("dateQG6")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private ZonedDateTime dateQG6;

        @JsonProperty("dueDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate dueDate;

        @JsonProperty("modifiedAt")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private ZonedDateTime modifiedAt;

        @JsonProperty("projectName")
        private String projectName;

        @JsonProperty("machineSerialNumber")
        private String machineSerialNumber;

        @JsonProperty("projectNotes")
        private String projectNotes;

        @JsonProperty("ownerName")
        private String ownerName;

        @JsonProperty("permalinkUrl")
        private String permalinkUrl;

        @JsonProperty("startOn")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate startOn;
    }

    @Data
    public static class AsanaTask {
        @JsonProperty("asanaTaskId")
        private String asanaTaskId;

        @JsonProperty("assigneeName")
        private String assigneeName;

        @JsonProperty("taskCompleted")
        private Boolean taskCompleted;

        @JsonProperty("completedAt")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private ZonedDateTime completedAt;

        @JsonProperty("createdAt")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private ZonedDateTime createdAt;

        @JsonProperty("dueDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate dueDate;

        @JsonProperty("modifiedAt")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private ZonedDateTime modifiedAt;

        @JsonProperty("taskName")
        private String taskName;

        @JsonProperty("taskNotes")
        private String taskNotes;

        @JsonProperty("permalinkUrl")
        private String permalinkUrl;

        @JsonProperty("relatedProjects")
        private List<RelatedProject> relatedProjects;

        @JsonProperty("startTimestamp")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private ZonedDateTime startTimestamp;

        @JsonProperty("delayCause")
        private String delayCause;

        @JsonProperty("targetDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate targetDate;

        @JsonProperty("projectEngineer") 
        private String projectEngineer;

        @JsonProperty("projectManager")
        private String projectManager;
    }

    @Data
    public static class RelatedProject {
        @JsonProperty("asanaProjectId")
        private String asanaProjectId;

        @JsonProperty("projectName")
        private String projectName;
    }
}
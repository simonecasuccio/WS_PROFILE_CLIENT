package it.s3k.fedegari.profile.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProfileTask {
    private String profileTaskId;
    private String asanaTaskId;
    private String assigneeName;
    private Boolean taskCompleted;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime completedAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime modifiedAt;
    
    private String taskName;
    private String taskNotes;
    private String permalinkUrl;
    
    @Data
    public static class RelatedProject {
        private String asanaProjectId;
        private String projectName;
    }
    
    private List<RelatedProject> relatedProjects;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime startTimestamp;
    
    private String delayCause;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate targetDate;
    
    private String projectEngineer;
    private String projectManager;
}
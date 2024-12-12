package it.s3k.fedegari.profile.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.Map;

@Data
public class TaskRequest {
    private String name;
    private String description;
    private String assignee;
    private String projectId;
    private String status;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime plannedStartDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime plannedFinishDate;
    
    private Map<String, Object> customFields;
}
package it.s3k.fedegari.profile.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.Map;

@Data
public class TaskDTO {
    private String id;
    private String name;
    private String description;
    private String status;
    private String assignee;
    private String projectId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime plannedStartDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime plannedFinishDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime actualStartDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime actualFinishDate;
    
    private Map<String, Object> customFields;
}
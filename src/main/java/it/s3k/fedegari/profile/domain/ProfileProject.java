package it.s3k.fedegari.profile.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.ZonedDateTime;
import java.time.LocalDate;

@Data
public class ProfileProject {
    private String profileProjectId;
    private String asanaProjectId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime createdAt;
    
    private String statusColor;
    private String statusUpdate;
    private String projectEngineer;
    private String machineModel;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime dateQG5;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime dateQG6;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private ZonedDateTime modifiedAt;
    
    private String projectName;
    private String machineSerialNumber;
    private String projectNotes;
    private String ownerName;
    private String permalinkUrl;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startOn;
}
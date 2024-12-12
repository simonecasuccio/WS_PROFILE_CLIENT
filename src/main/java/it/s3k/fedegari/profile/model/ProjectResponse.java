package it.s3k.fedegari.profile.model;

import java.util.List;

import lombok.Data;

@Data
public class ProjectResponse {
    private String correlationId;
    private String resultCode;
    private String resultDescription;
    private List<PlmProjectData> plmProjectData;
    private List<PlmTaskData> plmTaskData;
}
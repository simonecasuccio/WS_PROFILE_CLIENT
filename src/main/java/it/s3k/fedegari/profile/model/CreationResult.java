package it.s3k.fedegari.profile.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CreationResult {
    private String resultCode;
    private String resultDescription;
    private List<ProjectCreationResult> projectResults = new ArrayList<>();
    private List<TaskCreationResult> taskResults = new ArrayList<>();
}
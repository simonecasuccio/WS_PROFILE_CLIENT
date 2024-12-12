package it.s3k.fedegari.profile.model;

import lombok.Data;

@Data
public class TaskResponse {
    private String profileTaskId;
    private boolean success;
    private String errorMessage;
}
package it.s3k.fedegari.profile.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import it.s3k.fedegari.profile.client.ProfileRestClient;
import it.s3k.fedegari.profile.domain.ProfileProject;
import it.s3k.fedegari.profile.domain.ProfileTask;
import it.s3k.fedegari.profile.exception.ProfileApiException;
import it.s3k.fedegari.profile.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ProfileService {

    private final ProfileRestClient profileClient;
    private final ObjectMapper objectMapper;

    public ProfileService(ProfileRestClient profileClient, ObjectMapper objectMapper) {
        this.profileClient = profileClient;
        this.objectMapper = objectMapper;
    }

    public ProfileProject getProject(String projectId) throws Exception {
        log.debug("Getting project with ID: {}", projectId);

        JSONArray requestArray = new JSONArray();
        JSONObject entityObject = new JSONObject();
        entityObject.put("key", "urn:key:Project:" + projectId);
        requestArray.put(entityObject);

        String response = profileClient.executeRequest(
                "/LoadEntity",
                "POST",
                requestArray.toString());

        JSONArray jsonArray = new JSONArray(response);
        if (jsonArray.length() > 0) {
            JSONObject entityResponse = jsonArray.getJSONObject(0);

            ProfileProject project = new ProfileProject();
            project.setAsanaProjectId(projectId);

            if (entityResponse.has("FieldValues")) {
                JSONObject fieldValues = entityResponse.getJSONObject("FieldValues");
                project.setProjectName(fieldValues.optString("/6/2"));
                project.setStatusColor(fieldValues.optString("/6/3"));
                project.setStatusUpdate(fieldValues.optString("/6/4"));
                project.setProjectEngineer(fieldValues.optString("/6/5"));
                project.setMachineModel(fieldValues.optString("/6/6"));
                project.setMachineSerialNumber(fieldValues.optString("/6/7"));
                project.setProjectNotes(fieldValues.optString("/6/8"));
                project.setOwnerName(fieldValues.optString("/6/9"));
                project.setPermalinkUrl(fieldValues.optString("/6/10"));

                String dateQG5 = fieldValues.optString("/6/11");
                if (!dateQG5.isEmpty()) {
                    project.setDateQG5(ZonedDateTime.parse(dateQG5));
                }

                String dateQG6 = fieldValues.optString("/6/12");
                if (!dateQG6.isEmpty()) {
                    project.setDateQG6(ZonedDateTime.parse(dateQG6));
                }

                String dueDate = fieldValues.optString("/6/13");
                if (!dueDate.isEmpty()) {
                    project.setDueDate(LocalDate.parse(dueDate));
                }

                String startOn = fieldValues.optString("/6/14");
                if (!startOn.isEmpty()) {
                    project.setStartOn(LocalDate.parse(startOn));
                }
            }

            return project;
        }

        throw new EntityNotFoundException("Project not found with ID: " + projectId);
    }

    public ProfileTask getTask(String taskId) throws Exception {
        log.debug("Getting task with ID: {}", taskId);

        JSONArray requestArray = new JSONArray();
        JSONObject entityObject = new JSONObject();
        entityObject.put("key", "urn:key:Task:" + taskId);
        requestArray.put(entityObject);

        String response = profileClient.executeRequest(
                "/LoadEntity",
                "POST",
                requestArray.toString());

        JSONArray jsonArray = new JSONArray(response);
        if (jsonArray.length() > 0) {
            JSONObject entityResponse = jsonArray.getJSONObject(0);

            ProfileTask task = new ProfileTask();
            task.setAsanaTaskId(taskId);

            if (entityResponse.has("FieldValues")) {
                JSONObject fieldValues = entityResponse.getJSONObject("FieldValues");
                task.setTaskName(fieldValues.optString("/31/1"));
                task.setAssigneeName(fieldValues.optString("/31/12"));
                task.setTaskCompleted(fieldValues.optBoolean("/31/3"));
                task.setTaskNotes(fieldValues.optString("/31/2"));
                task.setPermalinkUrl(fieldValues.optString("/31/5"));
                task.setProjectEngineer(fieldValues.optString("/31/6"));
                task.setProjectManager(fieldValues.optString("/31/7"));
                task.setDelayCause(fieldValues.optString("/31/8"));

                String completedAt = fieldValues.optString("/31/9");
                if (!completedAt.isEmpty()) {
                    task.setCompletedAt(ZonedDateTime.parse(completedAt));
                }

                String createdAt = fieldValues.optString("/31/10");
                if (!createdAt.isEmpty()) {
                    task.setCreatedAt(ZonedDateTime.parse(createdAt));
                }

                String dueDate = fieldValues.optString("/31/11");
                if (!dueDate.isEmpty()) {
                    task.setDueDate(LocalDate.parse(dueDate));
                }

                String modifiedAt = fieldValues.optString("/31/102");
                if (!modifiedAt.isEmpty()) {
                    task.setModifiedAt(ZonedDateTime.parse(modifiedAt));
                }
            }

            return task;
        }

        throw new EntityNotFoundException("Task not found with ID: " + taskId);
    }

    public ProfileProject getProjectByAsanaId(String projectId) throws Exception {
        log.debug("Getting project with Asana ID: {}", projectId);

        JSONArray requestArray = new JSONArray();
        JSONObject request = new JSONObject();
        request.put("collectionQuery",
                "(context) => context.Projects().WithFilterOption(new FieldValues { {\"/6/51\", \"" + projectId
                        + "\"} })");
        requestArray.put(request);

        String response = profileClient.executeRequest(
                "/ExecuteCollectionQuery",
                "POST",
                requestArray.toString());

        JSONArray jsonArray = new JSONArray(response);
        if (jsonArray.length() >= 3 && jsonArray.getString(0).equals("BeginOfEnumerable")) {
            JSONArray entities = jsonArray.getJSONArray(1);
            if (entities.length() > 0) {
                JSONObject entityResponse = entities.getJSONObject(0);

                ProfileProject project = new ProfileProject();
                project.setAsanaProjectId(projectId);
                project.setProfileProjectId(entityResponse.getString("Key").replace("urn:key:Project:", ""));


                if (entityResponse.has("FieldValues")) {
                    JSONObject fieldValues = entityResponse.getJSONObject("FieldValues");
                    project.setProjectName(fieldValues.optString("/6/2"));
                    project.setStatusUpdate(fieldValues.optString("/6/4"));
                    project.setProjectEngineer(fieldValues.optString("/6/5"));
                    project.setProjectNotes(fieldValues.optString("/6/23"));
                    project.setPermalinkUrl(fieldValues.optString("/6/59"));
                    project.setMachineModel(fieldValues.optString("/6/99"));
                    project.setMachineSerialNumber(fieldValues.optString("/6/100"));
                    project.setOwnerName(fieldValues.optString("/6/101"));

                    String startOn = fieldValues.optString("/6/18");
                    if (!startOn.isEmpty()) {
                        project.setStartOn(LocalDate.parse(startOn.substring(0, 10)));
                    }

                    String dueDate = fieldValues.optString("/6/19");
                    if (!dueDate.isEmpty()) {
                        project.setDueDate(LocalDate.parse(dueDate.substring(0, 10)));
                    }
                }

                return project;
            }
        }

        throw new EntityNotFoundException("Project not found with Asana ID: " + projectId);
    }

    public ProfileTask getTaskByAsanaId(String taskId) throws Exception {
        log.debug("Getting task with Asana ID: {}", taskId);

        JSONArray requestArray = new JSONArray();
        JSONObject request = new JSONObject();
        request.put("collectionQuery",
                "(context) => context.Tasks().WithFilterOption(new FieldValues { {\"/31/100\", \"" + taskId + "\"} })");
        requestArray.put(request);

        String response = profileClient.executeRequest(
                "/ExecuteCollectionQuery",
                "POST",
                requestArray.toString());

        JSONArray jsonArray = new JSONArray(response);

        if (jsonArray.length() >= 3 && jsonArray.getString(0).equals("BeginOfEnumerable")) {
            JSONArray entities = jsonArray.getJSONArray(1);
            if (entities.length() > 0) {
                JSONObject entityResponse = entities.getJSONObject(0);

                ProfileTask task = new ProfileTask();
                task.setAsanaTaskId(taskId);
                task.setProfileTaskId(entityResponse.getString("Key").replace("urn:key:Task:", ""));


                if (entityResponse.has("FieldValues")) {
                    JSONObject fieldValues = entityResponse.getJSONObject("FieldValues");
                    task.setTaskName(fieldValues.optString("/31/1"));
                    task.setAssigneeName(fieldValues.optString("/31/12"));
                    task.setTaskCompleted(fieldValues.optBoolean("/31/3"));
                    task.setTaskNotes(fieldValues.optString("/31/2"));
                    task.setPermalinkUrl(fieldValues.optString("/31/5"));
                    task.setProjectEngineer(fieldValues.optString("/31/6"));
                    task.setProjectManager(fieldValues.optString("/31/7"));
                    task.setDelayCause(fieldValues.optString("/31/8"));

                    String completedAt = fieldValues.optString("/31/9");
                    if (!completedAt.isEmpty()) {
                        task.setCompletedAt(ZonedDateTime.parse(completedAt));
                    }

                    String createdAt = fieldValues.optString("/31/10");
                    if (!createdAt.isEmpty()) {
                        task.setCreatedAt(ZonedDateTime.parse(createdAt));
                    }

                    String dueDate = fieldValues.optString("/31/11");
                    if (!dueDate.isEmpty()) {
                        task.setDueDate(LocalDate.parse(dueDate));
                    }

                    String modifiedAt = fieldValues.optString("/31/102");
                    if (!modifiedAt.isEmpty()) {
                        task.setModifiedAt(ZonedDateTime.parse(modifiedAt));
                    }
                }

                return task;
            }
        }
        throw new EntityNotFoundException("Task not found with Asana ID: " + taskId);
    }

    public String createProject(JSONObject fields) throws Exception {
        log.debug("Creating project with fields: {}", fields);

        JSONArray requestArray = new JSONArray();
        JSONObject entityObject = new JSONObject();
        entityObject.put("classification", new JSONArray().put("/Project/"));
        entityObject.put("fieldValues", fields);
        requestArray.put(entityObject);

        String response = profileClient.executeRequest(
                "/CreateEntity",
                "POST",
                requestArray.toString());

        JSONArray jsonArray = new JSONArray(response);
        if (jsonArray.length() > 0) {
            JSONObject entityResponse = jsonArray.getJSONObject(0);
            String projectId = entityResponse.getString("Key").replace("urn:key:Project:", "");
            log.info("Project created successfully with ID: {}", projectId);
            return projectId;
        }

        throw new ProfileApiException("Empty response from Project Profile API", 500, response);
    }

    public String createTask(JSONObject fields) throws Exception {
        log.debug("Creating task with fields: {}", fields);

        JSONArray requestArray = new JSONArray();
        JSONObject entityObject = new JSONObject();
        entityObject.put("classification", new JSONArray().put("/Task/"));
        entityObject.put("fieldValues", fields);
        requestArray.put(entityObject);

        String response = profileClient.executeRequest(
                "/CreateEntity",
                "POST",
                requestArray.toString());

        JSONArray jsonArray = new JSONArray(response);
        if (jsonArray.length() > 0) {
            JSONObject entityResponse = jsonArray.getJSONObject(0);
            String taskId = entityResponse.getString("Key").replace("urn:key:Task:", "");
            log.info("Task created successfully with ID: {}", taskId);
            return taskId;
        }

        throw new ProfileApiException("Empty response from Task Profile API", 500, response);
    }

    public void createProjectTaskLink(String projectId, String taskId) throws Exception {
        log.debug("Creating link between project {} and task {}", projectId, taskId);

        JSONArray requestArray = new JSONArray();
        JSONObject entityObject = new JSONObject();
        entityObject.put("classification", new JSONArray().put("/Relationship/"));
        entityObject.put("parentKey", "urn:key:Project:" + projectId);
        entityObject.put("childKey", "urn:key:Task:" + taskId);
        entityObject.put("fieldValues", new JSONObject());
        requestArray.put(entityObject);

        profileClient.executeRequest(
                "/CreateEdgeEntity",
                "POST",
                requestArray.toString());

        log.info("Link created successfully between project {} and task {}", projectId, taskId);
    }

    public void updateProject(String projectId, JSONObject fields) throws Exception {
        log.debug("Updating project {} with fields: {}", projectId, fields);

        JSONArray requestArray = new JSONArray();
        JSONObject entityObject = new JSONObject();
        entityObject.put("key", "urn:key:Project:" + projectId);
        entityObject.put("fieldValues", fields);
        requestArray.put(entityObject);

        profileClient.executeRequest(
                "/UpdateEntity",
                "POST",
                requestArray.toString());

        log.info("Project {} updated successfully", projectId);
    }

    public void updateTask(String taskId, JSONObject fields) throws Exception {
        log.debug("Updating task {} with fields: {}", taskId, fields);

        JSONArray requestArray = new JSONArray();
        JSONObject entityObject = new JSONObject();
        entityObject.put("key", "urn:key:Task:" + taskId);
        entityObject.put("fieldValues", fields);
        requestArray.put(entityObject);

        profileClient.executeRequest(
                "/UpdateEntity",
                "POST",
                requestArray.toString());

        log.info("Task {} updated successfully", taskId);
    }

    public List<ProfileTask> getModifiedTasks(ZonedDateTime since, String projectId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("(context) => context")
                .append(".Tasks()")
                .append(".Where(task => task.ModifiedAt > DateTime.Parse(\"")
                .append(since.format(DateTimeFormatter.ISO_DATE_TIME))
                .append("\"))");

        if (projectId != null) {
            query.append(" && task.AsanaProjectId == \"").append(projectId).append("\"");
        }

        JSONObject request = new JSONObject();
        request.put("collectionQuery", query.toString());

        String response = profileClient.executeRequest(
                "/ExecuteCollectionQuery",
                "POST",
                request.toString());

        JSONArray jsonResponse = new JSONArray(response);
        return objectMapper.readValue(
                jsonResponse.toString(),
                new TypeReference<List<ProfileTask>>() {
                });
    }

    private ZonedDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return ZonedDateTime.parse(dateStr);
    }
}
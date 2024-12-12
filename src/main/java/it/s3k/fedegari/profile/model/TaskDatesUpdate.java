package it.s3k.fedegari.profile.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class TaskDatesUpdate {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime actualStartDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime actualFinishDate;
}
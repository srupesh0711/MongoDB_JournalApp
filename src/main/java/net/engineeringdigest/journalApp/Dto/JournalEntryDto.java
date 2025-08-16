package net.engineeringdigest.journalApp.Dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
public class JournalEntryDto {
    private ObjectId id;
    private String title;
    private String content;
    private LocalDateTime date;
}

package net.engineeringdigest.journalApp.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class JournalEntryDto {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Content cannot be blank")
    private String content;
}

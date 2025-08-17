package net.engineeringdigest.journalApp.Dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class TestSmsRequest {
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
}
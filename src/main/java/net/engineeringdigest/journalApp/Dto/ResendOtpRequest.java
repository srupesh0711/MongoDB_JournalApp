package net.engineeringdigest.journalApp.Dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ResendOtpRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;
}
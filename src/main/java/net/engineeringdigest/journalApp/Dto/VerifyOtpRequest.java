package net.engineeringdigest.journalApp.Dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class VerifyOtpRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "OTP cannot be blank")
    private String otp;
}
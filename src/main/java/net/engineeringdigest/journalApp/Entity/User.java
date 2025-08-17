package net.engineeringdigest.journalApp.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private List<String> roles;
    private String phoneNumber;
    private String otp;
    private LocalDateTime otpGeneratedTime;
    private boolean enabled;
}
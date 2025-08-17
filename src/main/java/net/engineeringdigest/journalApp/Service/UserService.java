package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.Dto.RegistrationRequest;
import net.engineeringdigest.journalApp.Entity.User;
import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.Exceptions.UserAlreadyExistsException;
import net.engineeringdigest.journalApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;

    @Value("${otp.expiration.minutes}")
    private long otpExpirationMinutes;

    public void registerUser(RegistrationRequest request) {
        User existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser != null && existingUser.isEnabled()) {
            throw new UserAlreadyExistsException("User with username '" + request.getUsername() + "' already exists.");
        }

        String otp = generateOtp();
        String message = "Your JournalApp OTP is: " + otp;

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber().replaceAll("\\s+", ""));

        if (existingUser != null) { // User exists but is not enabled
            updateUnverifiedUser(existingUser, user.getPassword(), user.getPhoneNumber(), otp);
        } else { // New user
            createNewUser(user, otp);
        }

        smsService.sendSms(user.getPhoneNumber(), message);
    }

    public void verifyUser(String username, String otp) {
        User user = userRepository.findByUsername(username);
        if (user == null || user.isEnabled()) {
            throw new IllegalArgumentException("User not found or is already verified.");
        }

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP.");
        }

        if (user.getOtpGeneratedTime().plusMinutes(otpExpirationMinutes).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired.");
        }

        user.setEnabled(true);
        user.setOtp(null);
        user.setOtpGeneratedTime(null);
        userRepository.save(user);
    }

    private void createNewUser(User user, String otp) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        user.setEnabled(false);
        userRepository.save(user);
    }

    private void updateUnverifiedUser(User existingUser, String newPassword, String newPhoneNumber, String otp) {
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setPhoneNumber(newPhoneNumber);
        existingUser.setOtp(otp);
        existingUser.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(existingUser);
    }

    private String generateOtp() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }
    public void resendOtp(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null || user.isEnabled()) {
            // To prevent user enumeration, we don't reveal if the user exists or is already verified.
            throw new IllegalArgumentException("Cannot resend OTP for this user.");
        }
        String otp = generateOtp();
        String message = "Your new JournalApp OTP is: " + otp;

        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);

        smsService.sendSms(user.getPhoneNumber(), message);
    }

    public String sendTestSms(@NotBlank(message = "Phone number cannot be blank") String phoneNumber) {
        return smsService.sendTestSms(phoneNumber);
    }
}
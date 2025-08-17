package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.Dto.ApiResponse;
import net.engineeringdigest.journalApp.Dto.AuthenticationRequest;
import net.engineeringdigest.journalApp.Dto.AuthenticationResponse;
import net.engineeringdigest.journalApp.Dto.RegistrationRequest;
import net.engineeringdigest.journalApp.Dto.ResendOtpRequest;
import net.engineeringdigest.journalApp.Dto.TestSmsRequest;
import net.engineeringdigest.journalApp.Dto.VerifyOtpRequest;
import net.engineeringdigest.journalApp.Entity.User;
import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.Service.UserService;
import net.engineeringdigest.journalApp.Utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@Valid @RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        AuthenticationResponse tokenResponse = new AuthenticationResponse(jwt);
        return ResponseEntity.ok(new ApiResponse<>("success", "Login successful.", tokenResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> registerUser(@Valid @RequestBody RegistrationRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(new ApiResponse<>("success", "OTP sent to your phone number. Please verify.", null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        userService.verifyUser(request.getUsername(), request.getOtp());
        return ResponseEntity.ok(new ApiResponse<>("success", "User verified successfully. You can now log in.", null));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<Object>> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        userService.resendOtp(request.getUsername());
        return ResponseEntity.ok(new ApiResponse<>("success", "A new OTP has been sent to your phone number.", null));
    }

    @PostMapping("/test-sms")
    public ResponseEntity<ApiResponse<Object>> testSms(@Valid @RequestBody TestSmsRequest request) {
        String result = userService.sendTestSms(request.getPhoneNumber());
        return ResponseEntity.ok(new ApiResponse<>("success", result, null));
    }
}
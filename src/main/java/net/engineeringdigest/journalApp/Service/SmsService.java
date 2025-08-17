package net.engineeringdigest.journalApp.Service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    @PostConstruct
    private void initTwilio() {
        Twilio.init(accountSid, authToken);
        log.info("Twilio initialized with account SID: {}", accountSid);
    }

    @Async
    public void sendSms(String to, String messageBody) {
        log.info("Attempting to send SMS asynchronously to {}", to);
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(twilioPhoneNumber),
                    messageBody
            ).create();
            log.info("Successfully queued SMS with SID: {}. Check Twilio console for delivery status.", message.getSid());
        } catch (ApiException e) {
            log.error("Failed to send SMS asynchronously. Twilio Error Code: {}, Message: {}. Please check your Twilio credentials, account settings (e.g., geo-permissions), and trial account limitations.", e.getCode(), e.getMessage());
        }
    }

    /**
     * Sends a synchronous test SMS for debugging purposes.
     * This method is blocking and will throw an ApiException on failure.
     * @param to The recipient's phone number.
     * @return A success message with the message SID.
     */
    public String sendTestSms(String to) {
        log.info("Attempting to send synchronous test SMS to {}", to);
        Message message = Message.creator(
                new PhoneNumber(to), new PhoneNumber(twilioPhoneNumber), "This is a test message from JournalApp.").create();
        log.info("Successfully sent synchronous test SMS with SID: {}", message.getSid());
        return "Test message queued successfully with SID: " + message.getSid();
    }
}
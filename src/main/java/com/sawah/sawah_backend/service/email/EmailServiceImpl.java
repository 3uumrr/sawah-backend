package com.sawah.sawah_backend.service.email;

import com.sawah.sawah_backend.exceptions.EmailDeliveryException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Override
    @Async
    public void sendVerificationCode(String to, String code, Locale locale) {
        String subject = messageSource.getMessage("mail.reset.subject", null, "Password Reset", locale);
        String text = messageSource.getMessage("mail.reset.body", new Object[]{code}, "Your code is: " + code, locale);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailDeliveryException("error.mail.send.failed");
        }
    }

}

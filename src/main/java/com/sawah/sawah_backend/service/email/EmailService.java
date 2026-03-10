package com.sawah.sawah_backend.service.email;

import java.util.Locale;

public interface EmailService {
    void sendVerificationCode(String to, String code , Locale locale);
}

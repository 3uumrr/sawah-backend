package com.sawah.sawah_backend.helper;


import java.security.SecureRandom;

public class OtpGenerator {
    public static String generateOtp(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10)); // يولد رقم بين 0 و 9
        }

        return otp.toString();
    }
}
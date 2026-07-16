package com.sawah.sawah_backend.service.aiService.landmark;

import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

public interface LandmarkService {
    String exploreLandmarkFromImage(MultipartFile file, Locale locale);
}

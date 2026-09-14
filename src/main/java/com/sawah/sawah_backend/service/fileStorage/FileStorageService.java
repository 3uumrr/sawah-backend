package com.sawah.sawah_backend.service.fileStorage;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageService {
    String storeFile(String dir, MultipartFile file) throws IOException;
    void deleteFile(String dir, String fileName);
}

package com.sawah.sawah_backend.service.fileStorage;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageService {
    String storeFile(MultipartFile file, String dir) throws IOException;
    void deleteFile(String fileName , String dir);
}

package com.sawah.sawah_backend.service.fileStorage;

import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService{

    private static final String USER_UPLOAD_DIR = "user_photos/";
    private static final String CATEGORY_ICON_DIR = "category_icons/";


    public FileStorageServiceImpl(){
        try {
            Files.createDirectories(Paths.get(USER_UPLOAD_DIR));
            Files.createDirectories(Paths.get(CATEGORY_ICON_DIR));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String storeFile(MultipartFile file , String dir) throws IOException {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString() + "." + extension;
        Path rootPath = Paths.get(dir);

        Path targetPath = rootPath.resolve(filename).normalize();
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    @Override
    public void deleteFile(String fileName , String dir) {
        if (fileName == null || fileName.isEmpty()) return;

        try {

            Path rootPath = Paths.get(dir);

            Path filePath = rootPath.resolve(fileName).normalize();

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("File deleted: " + fileName);
            } else {
                throw new ResourceNotFoundException("File not found: " + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + fileName, e);
        }
    }

}

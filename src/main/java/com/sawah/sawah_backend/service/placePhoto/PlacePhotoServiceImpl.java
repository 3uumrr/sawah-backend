package com.sawah.sawah_backend.service.placePhoto;

import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.Place;
import com.sawah.sawah_backend.models.PlacePhoto;
import com.sawah.sawah_backend.repository.PlacePhotoRepository;
import com.sawah.sawah_backend.repository.PlaceRepository;
import com.sawah.sawah_backend.service.fileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlacePhotoServiceImpl implements PlacePhotoService {

    private static final String PLACE_PHOTO_DIR = "place_photos/";

    private final PlacePhotoRepository placePhotoRepository;
    private final PlaceRepository placeRepository;
    private final FileStorageService fileStorageService;

    @Override
    public PlacePhoto getById(Long id) {
        return placePhotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("place.photo.not.found"));
    }


    @Override
    public List<PlacePhoto> getByPlaceId(Long placeId) {
        return placePhotoRepository.findByPlaceIdOrderByDisplayOrderAsc(placeId);
    }


    @Override
    @Transactional
    public String
    create(MultipartFile file, Integer displayOrder, Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("place.not.found"));

        String fileName = storePhoto(file);

        PlacePhoto placePhoto = PlacePhoto.builder()
                .url(fileName)
                .displayOrder(defaultDisplayOrder(displayOrder))
                .place(place)
                .build();

        placePhotoRepository.save(placePhoto);

        return fileName;
    }

    @Override
    @Transactional
    public void update(Long id, MultipartFile file, Integer displayOrder) {
        PlacePhoto placePhoto = getById(id);

        if (file != null && !file.isEmpty()) {
            String oldUrl = placePhoto.getUrl();
            String newUrl = storePhoto(file);

            fileStorageService.deleteFile(PLACE_PHOTO_DIR, oldUrl);
            placePhoto.setUrl(newUrl);
        }

        placePhoto.setDisplayOrder(defaultDisplayOrder(displayOrder));

        placePhotoRepository.save(placePhoto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PlacePhoto placePhoto = getById(id);

        fileStorageService.deleteFile(PLACE_PHOTO_DIR, placePhoto.getUrl());
        placePhotoRepository.delete(placePhoto);
    }

    @Override
    @Transactional
    public void deleteAllByPlaceId(Long placeId) {
        List<PlacePhoto> placePhotos = getByPlaceId(placeId);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for (PlacePhoto placePhoto : placePhotos) {
                    fileStorageService.deleteFile(PLACE_PHOTO_DIR, placePhoto.getUrl());
                }
            }
        });

        placePhotoRepository.deleteByPlaceId(placeId);
    }

    private String storePhoto(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("photo.upload.failed");
        }

        try {
            return fileStorageService.storeFile( PLACE_PHOTO_DIR, file);
        } catch (IOException e) {
            throw new RuntimeException("photo.upload.failed");
        }
    }

    private Boolean defaultIsPrimary(Boolean isPrimary) {
        return isPrimary != null ? isPrimary : false;
    }

    private Integer defaultDisplayOrder(Integer displayOrder) {
        return displayOrder != null ? displayOrder : 0;
    }
}

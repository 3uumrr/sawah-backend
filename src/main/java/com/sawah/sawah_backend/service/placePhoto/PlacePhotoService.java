package com.sawah.sawah_backend.service.placePhoto;

import com.sawah.sawah_backend.models.PlacePhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlacePhotoService {

    PlacePhoto getById(Long id);

    List<PlacePhoto> getByPlaceId(Long placeId);

    String create(MultipartFile file, Integer displayOrder, Long placeId);

    void update(Long id, MultipartFile file, Integer displayOrder);

    void delete(Long id);

    void deleteAllByPlaceId(Long placeId);
}

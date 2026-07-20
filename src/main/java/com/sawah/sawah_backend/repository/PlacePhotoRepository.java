package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.PlacePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlacePhotoRepository extends JpaRepository<PlacePhoto, Long> {

    List<PlacePhoto> findByPlaceIdOrderByDisplayOrderAsc(Long placeId);

    void deleteByPlaceId(Long placeId);

}

package com.sawah.sawah_backend.service.visitedPlace;

import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.Place;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.models.VisitedPlace;
import com.sawah.sawah_backend.repository.PlaceRepository;
import com.sawah.sawah_backend.repository.UserRepository;
import com.sawah.sawah_backend.repository.VisitedPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitedPlaceServiceImpl implements VisitedPlaceService {

    private final VisitedPlaceRepository visitedPlaceRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public void addVisitedPlace(Long userId, Long placeId) {
        User user = getUserById(userId);
        Place place = getPlaceById(placeId);

        if (visitedPlaceRepository.existsByUserIdAndPlaceId(userId, placeId)) {
            return;
        }

        VisitedPlace visitedPlace = VisitedPlace.builder()
                .user(user)
                .place(place)
                .build();

        visitedPlaceRepository.save(visitedPlace);
    }

    @Override
    @Transactional
    public void removeVisitedPlace(Long userId, Long placeId) {
        getUserById(userId);
        getPlaceById(placeId);

        visitedPlaceRepository.findByUserIdAndPlaceId(userId, placeId)
                .ifPresent(visitedPlaceRepository::delete);
    }

    @Override
    public Set<Long> findVisitedPlaceIds(Long userId, Collection<Long> placeIds) {
        if (placeIds == null || placeIds.isEmpty()) {
            return Set.of();
        }
        return visitedPlaceRepository.findPlaceIdsByUserIdAndPlaceIdIn(userId, placeIds);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user.not.found"));
    }

    private Place getPlaceById(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("place.not.found"));
    }
}

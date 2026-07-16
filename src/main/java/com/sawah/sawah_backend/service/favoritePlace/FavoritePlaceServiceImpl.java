package com.sawah.sawah_backend.service.favoritePlace;

import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.FavoritePlace;
import com.sawah.sawah_backend.models.Place;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.repository.FavoritePlaceRepository;
import com.sawah.sawah_backend.repository.PlaceRepository;
import com.sawah.sawah_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoritePlaceServiceImpl implements FavoritePlaceService {

    private final FavoritePlaceRepository favoritePlaceRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public void addFavoritePlace(Long userId, Long placeId) {
        User user = getUserById(userId);
        Place place = getPlaceById(placeId);

        if (favoritePlaceRepository.existsByUserIdAndPlaceId(userId, placeId)) {
            return;
        }

        FavoritePlace favoritePlace = FavoritePlace.builder()
                .user(user)
                .place(place)
                .build();

        favoritePlaceRepository.save(favoritePlace);
    }

    @Override
    public boolean existsByUserIdAndPlaceId(Long userId, Long placeId) {
        return favoritePlaceRepository.existsByUserIdAndPlaceId(userId, placeId);
    }

    @Override
    public Set<Long> findFavoritePlaceIds(Long userId, Collection<Long> placeIds) {
        if (placeIds == null || placeIds.isEmpty()) {
            return Set.of();
        }
        return favoritePlaceRepository.findPlaceIdsByUserIdAndPlaceIdIn(userId, placeIds);
    }

    @Override
    @Transactional
    public void removeFavoritePlace(Long userId, Long placeId) {
        getUserById(userId);
        getPlaceById(placeId);

        favoritePlaceRepository.findByUserIdAndPlaceId(userId, placeId)
                .ifPresent(favoritePlaceRepository::delete);
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

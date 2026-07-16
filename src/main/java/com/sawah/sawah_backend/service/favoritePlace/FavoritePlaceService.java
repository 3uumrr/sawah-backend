package com.sawah.sawah_backend.service.favoritePlace;

import java.util.Collection;
import java.util.Set;

public interface FavoritePlaceService {

    void addFavoritePlace(Long userId, Long placeId);
    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    void removeFavoritePlace(Long userId, Long placeId);

    Set<Long> findFavoritePlaceIds(Long userId, Collection<Long> placeIds);
}

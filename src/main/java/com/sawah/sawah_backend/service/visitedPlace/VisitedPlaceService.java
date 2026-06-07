package com.sawah.sawah_backend.service.visitedPlace;

import java.util.Collection;
import java.util.Set;

public interface VisitedPlaceService {

    void addVisitedPlace(Long userId, Long placeId);

    void removeVisitedPlace(Long userId, Long placeId);

    Set<Long> findVisitedPlaceIds(Long userId, Collection<Long> placeIds);
}

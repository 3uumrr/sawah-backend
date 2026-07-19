package com.sawah.sawah_backend.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class PlaceDetailsContext {
    private final Set<Long> favoritePlaceIds;
    private final Set<Long> visitedPlaceIds;
}

package com.sawah.sawah_backend.service.recentSearch;

public interface RecentSearchService {

    void addRecentSearch(Long userId, Long placeId);

    void clearRecentSearches(Long userId);

    void deleteRecentSearchById(Long id, Long userId);
    
}

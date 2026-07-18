package com.sawah.sawah_backend.service.recentSearch;

import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.Place;
import com.sawah.sawah_backend.models.RecentSearch;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.repository.RecentSearchRepository;
import com.sawah.sawah_backend.service.place.PlaceService;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecentSearchServiceImpl implements RecentSearchService {

    private final RecentSearchRepository recentSearchRepository;
    private final UserService userService;
    private final PlaceService placeService;

    @Override
    @Transactional
    public void addRecentSearch(Long userId, Long placeId) {
        User user = userService.getUserById(userId);
        Place place = placeService.getPlaceById(placeId);

        recentSearchRepository.findByUserIdAndPlaceId(userId, placeId).ifPresentOrElse(
                existingSearch -> {
                    existingSearch.setCreatedAt(LocalDateTime.now());
                    recentSearchRepository.save(existingSearch);
                },
                () -> {
                    RecentSearch recentSearch = RecentSearch.builder()
                            .user(user)
                            .place(place)
                            .build();
                    recentSearchRepository.save(recentSearch);
                }
        );
    }


    @Override
    @Transactional
    public void clearRecentSearches(Long userId) {
        recentSearchRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteRecentSearchById(Long id, Long userId) {
        RecentSearch recentSearch = recentSearchRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recent search not found"));
        recentSearchRepository.delete(recentSearch);
    }

}

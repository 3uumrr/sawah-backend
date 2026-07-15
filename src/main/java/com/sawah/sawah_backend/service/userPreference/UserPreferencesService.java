package com.sawah.sawah_backend.service.userPreference;

import com.sawah.sawah_backend.models.Category;

import java.util.List;

public interface UserPreferencesService {

    void addUserPreferences(Long userId, List<Long> categoryIds);

    List<Category> getUserCategories(Long userId);

    void deleteUserPreference(Long userId, Long categoryId);
}

package com.sawah.sawah_backend.service.userPreference;

import com.sawah.sawah_backend.models.Category;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.models.UserPreference;
import com.sawah.sawah_backend.models.UserPreferenceId;
import com.sawah.sawah_backend.repository.UserPreferenceRepository;
import com.sawah.sawah_backend.service.category.CategoryService;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPreferencesServiceImpl implements  UserPreferencesService
{
    private  final UserPreferenceRepository userPreferenceRepository;
    private final CategoryService categoryService;
    private  final UserService userService;


    @Override
    @Transactional
    public void addUserPreferences(Long userId, List<Long> categoryIds) {

        // Get User
        User user = userService.getUserById(userId);

        // GET Categories
        List<Category> categories = categoryService.findAllByIds(categoryIds);

        // Create UserPreferences
        Set<UserPreference> userPreferences = categories.stream().map(
                category -> UserPreference.builder()
                        .id(new UserPreferenceId(userId, category.getId()))
                        .user(user)
                        .category(category)
                        .build()
        ).collect(Collectors.toSet());


        // SAVE UserPreferences
        userPreferenceRepository.saveAll(userPreferences);
    }

    @Override
    public List<Category> getUserCategories(Long userId) {
        return categoryService.getCategoriesByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteUserPreference(Long userId, Long categoryId) {
        UserPreferenceId id = new UserPreferenceId(userId, categoryId);
        if (userPreferenceRepository.existsById(id)) {
            userPreferenceRepository.deleteById(id);
        }
    }
}

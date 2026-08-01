package com.sawah.sawah_backend.config;

import com.sawah.sawah_backend.service.category.CategoryService;
import com.sawah.sawah_backend.service.language.LanguageService;
import com.sawah.sawah_backend.service.role.RoleService;
import com.sawah.sawah_backend.service.service.ServiceService;
import com.sawah.sawah_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final ServiceService serviceService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LanguageService languageService;

    @Override
    public void run(String... args) {
        roleService.initRoles();
        serviceService.initServices();
        userService.initAdmin();
        categoryService.initCategories();
        languageService.initLanguages();
    }
}

package com.sawah.sawah_backend.service.category;

import com.sawah.sawah_backend.dto.category.CategoryInputDto;
import com.sawah.sawah_backend.models.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    void addCategory(CategoryInputDto categoryInputDto, MultipartFile file);
    void updateCategory(Long id, CategoryInputDto categoryInputDto, MultipartFile file);
    void deleteCategoryById(Long id);
    void initCategories();

    List<Category> getCategoriesByUserId(Long userId);

    List<Category> findAllByIds(List<Long> categoryIds);
}

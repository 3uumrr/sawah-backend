package com.sawah.sawah_backend.service.category;

import com.sawah.sawah_backend.dto.category.CategoryInputDto;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.mapper.CategoryMapper;
import com.sawah.sawah_backend.models.Category;
import com.sawah.sawah_backend.repository.CategoryRepository;
import com.sawah.sawah_backend.service.fileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;
    private final FileStorageService fileStorageService;
    private static final String CATEGORY_ICON_DIR = "category_icons/";

    @Override
    public Category getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category.not.found"));

    }

    @Override
    public List<Category> getAllCategories() {

        return categoryRepository.findAllByOrderByDisplayOrderAsc();

    }

    @Override
    @Transactional
    public void addCategory(CategoryInputDto categoryInputDto, MultipartFile file) {

        Category category = mapper.toEntity(categoryInputDto);

        try {
            category.setIconUrl(fileStorageService.storeFile(CATEGORY_ICON_DIR, file));
        } catch (IOException e) {
            throw new RuntimeException("photo.upload.failed");
        }

        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void updateCategory(Long id, CategoryInputDto categoryInputDto, MultipartFile file) {

        Category category = getCategoryById(id);

        mapper.updateCategory(categoryInputDto, category);

        if (file != null) {
            try {
                category.setIconUrl(fileStorageService.storeFile(CATEGORY_ICON_DIR, file));
            } catch (IOException e) {
                throw new RuntimeException("photo.upload.failed");
            }
        }

    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {

        Category category = getCategoryById(id);

        categoryRepository.delete(category);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                fileStorageService.deleteFile(CATEGORY_ICON_DIR, category.getIconUrl());
            }
        });

    }

    @Override
    @Transactional
    public void initCategories() {
        List<Category> categories = List.of(
                Category.builder()
                        .nameEn("Historical & Archaeological")
                        .nameAr("تاريخي وأثري")
                        .iconUrl("Historical.png")
                        .displayOrder(1)
                        .build(),

                Category.builder()
                        .nameEn("Religious")
                        .nameAr("ديني")
                        .iconUrl("Religious.png")
                        .displayOrder(2)
                        .build(),

                Category.builder()
                        .nameEn("Nature")
                        .nameAr("طبيعي")
                        .iconUrl("Nature.png")
                        .displayOrder(3)
                        .build(),

                Category.builder()
                        .nameEn("Entertainment")
                        .nameAr("ترفيهي")
                        .iconUrl("Entertainment.png")
                        .displayOrder(4)
                        .build()
        );

        for (Category category : categories) {
            if (categoryRepository.findCategoriesByNameEn(category.getNameEn()).isEmpty()) {
                categoryRepository.save(category);
            }
        }
    }

    @Override
    public List<Category> getCategoriesByUserId(Long userId) {
        return categoryRepository.findAllByUserId(userId);
    }

    @Override
    public List<Category> findAllByIds(List<Long> categoryIds) {
        return categoryRepository.findAllById(categoryIds);
    }

    @Override
    public Long categoriesCount() {
        return categoryRepository.count();
    }
}

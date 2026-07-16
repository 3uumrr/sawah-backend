package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.category.CategoryInputDto;
import com.sawah.sawah_backend.dto.category.CategoryResponseDto;
import com.sawah.sawah_backend.dto.user.UserInterestDto;
import com.sawah.sawah_backend.models.Category;
import org.mapstruct.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {
    Category toEntity(CategoryInputDto categoryInputDto);
    void updateCategory(CategoryInputDto categoryInputDto, @MappingTarget Category category);

    @Mapping(target = "iconUrl", source = "category", qualifiedByName = "toFullUrl")
    CategoryResponseDto toResponseDto(Category category);

    List<CategoryResponseDto> toListResponseDto(List<Category> category);

    @Mapping(target = "iconUrl", source = "category", qualifiedByName = "toFullUrl")
    UserInterestDto toUserInterestDto(Category category);

    List<UserInterestDto> toListUserCategoryDto(List<Category> category);




    @Named("toFullUrl")
    default String toFullUrl(Category category) {
        if (category.getIconUrl() == null) return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/category_icons/")
                .path(category.getIconUrl())
                .toUriString();
    }
}

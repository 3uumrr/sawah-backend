package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByOrderByDisplayOrderAsc();

    Optional<Category> findCategoriesByNameEn(String nameEn);

    @Query("SELECT new com.sawah.sawah_backend.models.Category(c.nameEn,c.nameAr,c.iconUrl) " +
            "FROM Category c JOIN UserPreference up ON c.id = up.category.id " +
            "WHERE up.user.id = :userId")
    List<Category> findAllByUserId(@Param("userId") Long userId);

}


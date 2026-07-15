package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.models.UserPreference;
import com.sawah.sawah_backend.models.UserPreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, UserPreferenceId>
{
    @Modifying
    @Query("DELETE FROM UserPreference up WHERE up.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}

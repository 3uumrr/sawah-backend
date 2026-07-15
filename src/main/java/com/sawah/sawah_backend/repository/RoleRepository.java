package com.sawah.sawah_backend.repository;

import com.sawah.sawah_backend.enums.RoleName;
import com.sawah.sawah_backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}


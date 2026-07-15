package com.sawah.sawah_backend.service.role;

import com.sawah.sawah_backend.enums.RoleName;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.Role;
import com.sawah.sawah_backend.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(RoleName name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(()-> new ResourceNotFoundException(
                        String.format("Role with name: %s not found",name))
        );
        return role;
    }

    @Override
    @Transactional
    public void initRoles() {
        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
}

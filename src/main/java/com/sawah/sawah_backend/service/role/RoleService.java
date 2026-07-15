package com.sawah.sawah_backend.service.role;

import com.sawah.sawah_backend.enums.RoleName;
import com.sawah.sawah_backend.models.Role;

public interface RoleService {
    Role findByName(RoleName name);
    void initRoles();
}

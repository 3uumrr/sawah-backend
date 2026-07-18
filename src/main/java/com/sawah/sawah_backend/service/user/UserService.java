package com.sawah.sawah_backend.service.user;

import com.sawah.sawah_backend.dto.user.CompleteTouristProfileDto;
import com.sawah.sawah_backend.dto.user.UpdateUserDto;
import com.sawah.sawah_backend.dto.user.UserInputDto;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.requests.ChangePasswordRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Page<User> getAllUsers(Pageable pageable);
    User getUserById(Long id);
    User findUserByEmail(String email);
    User findUserByEmailWithRoles(String email);
    void addUser(UserInputDto user , String accountType) ;
    Page<User> getUsersByRoleName(String roleName, Pageable pageable);
    void changeAccountStatus(Long userId); // Active Or InActive
    void resetPassword(String email, String newPassword);
    void updatePassword(ChangePasswordRequest request ,Long id);
    void updateUser(UpdateUserDto userDto , Long userId , MultipartFile file);
    void deleteUser(Long userId);
    boolean existByEmail(String email);
    void completeProfile(CompleteTouristProfileDto completeProfileDto, MultipartFile file, String email);
    void changePreferredLanguage(Long userId); // AR Or EN
    Long countUsers();
    void initAdmin();

}

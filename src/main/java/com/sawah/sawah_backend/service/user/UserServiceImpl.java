package com.sawah.sawah_backend.service.user;

import com.sawah.sawah_backend.dto.user.CompleteTouristProfileDto;
import com.sawah.sawah_backend.dto.user.UpdateUserDto;
import com.sawah.sawah_backend.dto.user.UserInputDto;
import com.sawah.sawah_backend.enums.Gender;
import com.sawah.sawah_backend.enums.PreferredLanguage;
import com.sawah.sawah_backend.enums.RoleName;
import com.sawah.sawah_backend.enums.UserAccStatus;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.exceptions.UserAlreadyExistsException;
import com.sawah.sawah_backend.exceptions.UserRegistrationException;
import com.sawah.sawah_backend.mapper.UserMapper;
import com.sawah.sawah_backend.models.Provider;
import com.sawah.sawah_backend.models.User;
import com.sawah.sawah_backend.repository.ChatConversationRepository;
import com.sawah.sawah_backend.repository.ChatMessageRepository;
import com.sawah.sawah_backend.repository.DriverProfileRepository;
import com.sawah.sawah_backend.repository.FavoritePlaceRepository;
import com.sawah.sawah_backend.repository.ProviderLanguageRepository;
import com.sawah.sawah_backend.repository.ProviderRepository;
import com.sawah.sawah_backend.repository.ProviderReviewRepository;
import com.sawah.sawah_backend.repository.ReviewRepository;
import com.sawah.sawah_backend.repository.ServiceRequestRepository;
import com.sawah.sawah_backend.repository.UserPreferenceRepository;
import com.sawah.sawah_backend.repository.UserRepository;
import com.sawah.sawah_backend.repository.VisitedPlaceRepository;
import com.sawah.sawah_backend.requests.ChangePasswordRequest;
import com.sawah.sawah_backend.service.fileStorage.FileStorageService;
import com.sawah.sawah_backend.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private final UserRepository userRepository;

    private final UserPreferenceRepository userPreferenceRepository;

    private final PasswordEncoder passwordEncoder;

    private final FileStorageService fileStorageService;

    private final RoleService roleService;

    private final UserMapper mapper;

    private static final String USER_UPLOAD_DIR = "user_photos/";


    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAllWithRoles(pageable);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user.not.found"));

    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user.not.found"));
    }

    @Override
    public User findUserByEmailWithRoles(String email) {
        return userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new ResourceNotFoundException("user.not.found"));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserInputDto user , String accountType){
        if (existByEmail(user.email())){
            throw new UserAlreadyExistsException("user.email.already.exists");
        }

        User mappedUser = mapper.toEntity(user);

        mappedUser.setPassword(passwordEncoder.encode(user.password()));

        mappedUser.setRoles(Set.of(roleService.findByName(Enum.valueOf(RoleName.class,accountType))));
        
        mappedUser = userRepository.save(mappedUser);

    }

    @Override
    public Page<User> getUsersByRoleName(String roleName, Pageable pageable) {
        return userRepository.getUsersByRoleName(Enum.valueOf(RoleName.class,roleName.toUpperCase()), pageable);
    }

    @Override
    @Transactional
    public void changeAccountStatus(Long userId) {
        User user = getUserById(userId);
        if (user.getAccountStatus().equals(UserAccStatus.ACTIVE)){
            user.setAccountStatus(UserAccStatus.INACTIVE);
        } else {
            user.setAccountStatus(UserAccStatus.ACTIVE);
        }

        userRepository.save(user);
    }

    private void updateProfilePhotoLogic(User user, MultipartFile file) throws IOException {
        if (user.getProfilePictureUrl() != null) {
            fileStorageService.deleteFile(USER_UPLOAD_DIR,user.getProfilePictureUrl());
        }

        String fileName = fileStorageService.storeFile(USER_UPLOAD_DIR, file);
        user.setProfilePictureUrl(fileName);
    }

    @Override
    @Transactional
    public void resetPassword(String email, String newPassword) {
        User user = findUserByEmail(email);

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(ChangePasswordRequest request, Long id) {
        User user = getUserById(id);

        // Check if new pass = old pass in DB
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())){
            throw new RuntimeException("password.old.incorrect");
        }

        // Check if new pass = old pass in DB
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())){
            throw new RuntimeException("password.same.as.old");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(UpdateUserDto userDto, Long userId, MultipartFile file) {
        User userFromDb = getUserById(userId);

        try {
            mapper.updateEntityFromDto(userDto, userFromDb);

            if (userDto.name() != null && !userDto.name().isBlank()) {
                String[] nameParts = userDto.name().trim().split("\\s+", 2);
                userFromDb.setFirstName(nameParts[0]);
                userFromDb.setLastName(nameParts.length > 1 ? nameParts[1] : "");
            }

            if (file != null && !file.isEmpty()) {
                updateProfilePhotoLogic(userFromDb, file);
            }

        } catch (Exception e) {
            throw new RuntimeException("user.profile.update.error");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                fileStorageService.deleteFile(USER_UPLOAD_DIR, user.getProfilePictureUrl());
            }
        });

    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Override
    @Transactional
    public void completeProfile(CompleteTouristProfileDto completeProfileDto, MultipartFile file, String email) {

        User user = findUserByEmail(email);

        if (file != null && !file.isEmpty()) {
            try {
                String fileName = fileStorageService.storeFile(USER_UPLOAD_DIR, file);
                user.setProfilePictureUrl(fileName);
            } catch (Exception e) {
                throw new UserRegistrationException("photo.upload.failed");
            }
        }

        user.setPhoneNumber(completeProfileDto.phoneNumber());
        user.setCountry(completeProfileDto.country());
        user.setGender(Enum.valueOf(Gender.class,completeProfileDto.gender()));
        if (completeProfileDto.preferredLanguage() != null) {
            user.setPreferredLanguage(Enum.valueOf(PreferredLanguage.class, completeProfileDto.preferredLanguage()));
        }
        user.setIsProfileComplete(true);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePreferredLanguage(Long userId) {
        User user = getUserById(userId);

        if (user.getPreferredLanguage().equals(PreferredLanguage.EN)) {
            user.setPreferredLanguage(PreferredLanguage.AR);
        } else  {
            user.setPreferredLanguage(PreferredLanguage.EN);
        }

        userRepository.save(user);
    }

    @Override
    public Long countUsers() {
       return userRepository.count();
    }

    @Override
    @Transactional
    public void initAdmin() {
        if (!userRepository.existsByEmail(adminEmail)){
            User user = User.builder()
                    .firstName("Sawah")
                    .lastName("Admin")
                    .isProfileComplete(true)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(roleService.findByName(RoleName.ADMIN)))
                    .build();
            userRepository.save(user);
        }
    }
}

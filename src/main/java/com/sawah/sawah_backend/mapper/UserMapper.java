package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.user.UpdateUserDto;
import com.sawah.sawah_backend.dto.user.UserAdminResponseDto;
import com.sawah.sawah_backend.dto.user.UserInputDto;
import com.sawah.sawah_backend.dto.user.UserResponseDto;
import com.sawah.sawah_backend.models.Role;
import com.sawah.sawah_backend.models.User;
import org.mapstruct.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toEntity(UserInputDto userDto);

    @Mapping(target = "name", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "profilePictureUrl", source = "user", qualifiedByName = "toFullUrl")
    UserResponseDto toResponseDto(User user);

    void updateEntityFromDto(UpdateUserDto dto, @MappingTarget User entity);

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "name", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "profilePictureUrl", source = "user", qualifiedByName = "toFullUrl")
    UserAdminResponseDto toAdminResponseDto(User user);

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "name", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "profilePictureUrl", source = "user", qualifiedByName = "toFullUrl")
    List<UserAdminResponseDto> toListAdminResponseDto(List<User> user);


    @Named("toFullUrl")
    default String toFullUrl(User user) {
        if (user.getProfilePictureUrl() == null) return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user_photos/")
                .path(user.getProfilePictureUrl())
                .toUriString();
    }


    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}

package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.chatMessage.ChatMessageResponse;
import com.sawah.sawah_backend.models.ChatMessage;
import com.sawah.sawah_backend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ChatMessageMapper {

    @Mapping(source = "chatConversation.id", target = "conversationId")
    @Mapping(source = "chatConversation.user", target = "userImageUrl", qualifiedByName = "toUserImageUrl")
    ChatMessageResponse toMessageResponse(ChatMessage chatMessage);

    List<ChatMessageResponse> toMessageResponse(List<ChatMessage> chatMessage);

    @Named("toUserImageUrl")
    default String toUserImageUrl(User user) {
        if (user == null || user.getProfilePictureUrl() == null) {
            return null;
        }

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user_photos/")
                .path(user.getProfilePictureUrl())
                .toUriString();
    }
}

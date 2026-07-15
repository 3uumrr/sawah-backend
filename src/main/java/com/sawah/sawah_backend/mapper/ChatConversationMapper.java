package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.chatConversation.ChatConversationResponse;
import com.sawah.sawah_backend.models.ChatConversation;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ChatConversationMapper {
    ChatConversationResponse toConversationResponse(ChatConversation chatConversation);

    List<ChatConversationResponse> toConversationResponse(List<ChatConversation> chatConversations);
}

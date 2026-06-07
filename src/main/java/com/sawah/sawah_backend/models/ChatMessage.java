package com.sawah.sawah_backend.models;

import com.sawah.sawah_backend.enums.ChatSender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 30)
    private ChatSender sender;

    @Column(columnDefinition = "TEXT" , nullable = false)
    private String message;

    @Column(name = "created_at" , nullable = false , updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatConversation chatConversation;
}

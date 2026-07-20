package com.sawah.sawah_backend.service.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sawah.sawah_backend.exceptions.BadRequestException;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.models.Notification;
import com.sawah.sawah_backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final String USER_NOTIFICATION_DESTINATION = "/queue/notifications";

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void createAndSendNotification(
            Long userId,
            String titleKey,
            String bodyKey,
            List<String> bodyArgs,
            Long bookingId) {

        Notification notification = Notification.builder()
                .userId(userId)
                .titleKey(titleKey)
                .bodyKey(bodyKey)
                .bodyArgs(serializeBodyArgs(bodyArgs))
                .bookingId(bookingId)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        sendSocketNotification(userId, savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadNotifications(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("notification.not.found"));

        if (!notification.getUserId().equals(userId)) {
            throw new BadRequestException("notification.unauthorized.access");
        }

        notification.setRead(true);
    }

    private String serializeBodyArgs(List<String> bodyArgs) {
        if (bodyArgs == null || bodyArgs.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(bodyArgs);
        } catch (JsonProcessingException exception) {
            log.warn("Failed to serialize notification body args. Falling back to comma-separated values.", exception);
            return String.join(",", bodyArgs);
        }
    }

    private void sendSocketNotification(Long userId, Notification notification) {
        try {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    USER_NOTIFICATION_DESTINATION,
                    notification
            );
        } catch (Exception exception) {
            log.warn("Notification {} was saved but could not be delivered over WebSocket.", notification.getId(), exception);
        }
    }
}

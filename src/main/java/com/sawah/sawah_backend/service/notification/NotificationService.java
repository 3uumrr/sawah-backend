package com.sawah.sawah_backend.service.notification;

import com.sawah.sawah_backend.models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    void createAndSendNotification(Long userId, String titleKey, String bodyKey, List<String> bodyArgs, Long bookingId);

    Page<Notification> getUserNotifications(Long userId, Pageable pageable);

    long countUnreadNotifications(Long userId);

    void markAsRead(Long notificationId, Long userId);
}

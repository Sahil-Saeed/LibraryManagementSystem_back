package com.cts.notificationservice.repository;

import com.cts.notificationservice.model.Notification;
import com.cts.notificationservice.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId);
    List<Notification> findByMemberIdAndIsReadFalseOrderByCreatedAtDesc(Long memberId);
    List<Notification> findByDeliveryStatus(DeliveryStatus deliveryStatus);
}
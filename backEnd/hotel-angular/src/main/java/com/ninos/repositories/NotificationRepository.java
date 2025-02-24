package com.ninos.repositories;

import com.ninos.entities.Notification;
import com.ninos.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

package com.ninos.notification;

import com.ninos.dtos.NotificationDTO;

public interface NotificationService {

    void sendEmail(NotificationDTO notificationDTO);
    void sendSms();
    void sendWhatsApp();

}
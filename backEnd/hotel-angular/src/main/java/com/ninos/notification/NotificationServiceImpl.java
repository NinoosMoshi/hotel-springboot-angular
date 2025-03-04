package com.ninos.notification;

import com.ninos.dtos.NotificationDTO;
import com.ninos.entities.Notification;
import com.ninos.enums.NotificationType;
import com.ninos.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationRepository;

    @Async
    @Override
    public void sendEmail(NotificationDTO notificationDTO) {
        log.info("Sending email");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notificationDTO.getRecipient());
        message.setSubject(notificationDTO.getSubject());
        message.setText(notificationDTO.getBody());

        javaMailSender.send(message);

        // save to database
        Notification notificationToSave = Notification.builder()
                .recipient(notificationDTO.getRecipient())
                .subject(notificationDTO.getSubject())
                .body(notificationDTO.getBody())
                .bookingReference(notificationDTO.getBookingReference())
                .type(NotificationType.EMAIL)
                .build();

        notificationRepository.save(notificationToSave);
    }



    @Override
    public void sendSms() {

    }

    @Override
    public void sendWhatsApp() {

    }
}

package com.ninos;

import com.ninos.dtos.NotificationDTO;
import com.ninos.enums.NotificationType;
import com.ninos.notification.NotificationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HotelAngularApplication {

//	@Autowired
//	private NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(HotelAngularApplication.class, args);
	}

//	@PostConstruct
//		public void sendDummyEmail() {
//		NotificationDTO notificationDTO = NotificationDTO.builder()
//				.type(NotificationType.EMAIL)
//				.recipient("ninoosmoshi1@gmail.com")
//				.body("I am testing this from command line")
//				.subject("Testing Email")
//				.build();
//		notificationService.sendEmail(notificationDTO);
//	}

}

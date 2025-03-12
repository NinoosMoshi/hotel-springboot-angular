package com.ninos.services.impl;

import com.ninos.dtos.BookingDTO;
import com.ninos.dtos.NotificationDTO;
import com.ninos.dtos.Response;
import com.ninos.dtos.RoomDTO;
import com.ninos.entities.Booking;
import com.ninos.entities.Room;
import com.ninos.entities.User;
import com.ninos.enums.BookingStatus;
import com.ninos.enums.PaymentStatus;
import com.ninos.exceptions.InvalidBookingStateAndDateException;
import com.ninos.exceptions.NotFoundException;
import com.ninos.notification.NotificationService;
import com.ninos.repositories.BookingRepository;
import com.ninos.repositories.RoomRepository;
import com.ninos.services.BookingCodeGenerator;
import com.ninos.services.BookingService;
import com.ninos.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
//import org.thymeleaf.TemplateEngine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
//import org.thymeleaf.context.Context;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final BookingCodeGenerator bookingCodeGenerator;
//    private final TemplateEngine templateEngine;


    @Override
    public Response getAllBookings() {
        List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<BookingDTO> bookingDTOList = modelMapper.map(bookingList, new TypeToken<List<BookingDTO>>() {}.getType());
        for(BookingDTO bookingDTO : bookingDTOList) {
            bookingDTO.setUser(null);
            bookingDTO.setRoom(null);
        }
        return Response.builder()
                .status(200)
                .message("Successfully Getting All Bookings")
                .bookings(bookingDTOList)
                .build();
    }

    @Override
    public Response createBooking(BookingDTO bookingDTO) {

        User currentUser = userService.getCurrentLoggedInUser();

        Room room = roomRepository.findById(bookingDTO.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found"));

        if(bookingDTO.getCheckInDate().isBefore(LocalDate.now())) {
            throw new InvalidBookingStateAndDateException("CheckInDate cannot be before today date");
        }
        if(bookingDTO.getCheckOutDate().isBefore(bookingDTO.getCheckInDate())) {
            throw new InvalidBookingStateAndDateException("CheckOutDate cannot be before checkInDate");
        }
        if(bookingDTO.getCheckInDate().isEqual(bookingDTO.getCheckOutDate())) {
            throw new InvalidBookingStateAndDateException("CheckInDate cannot be equal to checkOutDate");
        }

        // validate room availability
        boolean isAvailable = bookingRepository.isRoomAvailable(room.getId(), bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());
        if(!isAvailable) {
            throw new InvalidBookingStateAndDateException("Room is not available for the selected date ranges");
        }
        // calculate the total price needed to pay for the stay
        BigDecimal totalPrice = calculateTotalPrice(room, bookingDTO);
        String bookingReference = bookingCodeGenerator.generateBookingReference();

        // create and save the booking
        Booking booking = new Booking();
        booking.setUser(currentUser);
        booking.setRoom(room);
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setBookingReference(bookingReference);
        booking.setBookingStatus(BookingStatus.BOOKED);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        bookingRepository.save(booking);

        // generate the payment url which will be sent via mail
        String paymentUrl = "http://localhost:3000/payment/" + bookingReference + "/" + totalPrice;

        log.info("Payment URL: {}", paymentUrl);

        // send notification via email
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(currentUser.getEmail())
                .subject("BOOKING CONFIRMATION")
                .body(String.format("Your booking has been created successfully. \n Please proceed with your payment using the payment link below " +
                        "\n%s", paymentUrl))
                .bookingReference(bookingReference)
                .build();

        notificationService.sendEmail(notificationDTO); // sending email

//        // Prepare Thymeleaf variables
//        Context context = new Context();
//        context.setVariable("username", currentUser.getFirstName());
//        context.setVariable("paymentUrl", paymentUrl);
//        context.setVariable("bookingReference", bookingReference);
//
//// Process the Thymeleaf template
//        String emailBody = templateEngine.process("email/booking-confirmation", context);
//
//// Send notification via email with Thymeleaf content
//        NotificationDTO notificationDTO = NotificationDTO.builder()
//                .recipient(currentUser.getEmail())
//                .subject("Booking Confirmation")
//                .body(emailBody)
//                .bookingReference(bookingReference)
//                .build();
//
//        notificationService.sendEmail(notificationDTO);


        return Response.builder()
                .status(200)
                .message("Booking has been created successfully")
                .booking(bookingDTO)
                .build();

    }

    @Override
    public Response findBookingByReferenceNo(String bookingReference) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new NotFoundException("Booking with reference No: " + bookingReference + " not found"));

        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);

        return Response.builder()
                .status(200)
                .message("Successfully Find Booking By Reference")
                .booking(bookingDTO)
                .build();
    }

    @Override
    public Response updateBooking(BookingDTO bookingDTO) {
        if(bookingDTO.getId() == null) throw new NotFoundException("Booking Id cannot be null");

        Booking existingBooking = bookingRepository.findById(bookingDTO.getId())
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if(bookingDTO.getBookingStatus() != null){
            existingBooking.setBookingStatus(bookingDTO.getBookingStatus());
        }

        if(bookingDTO.getPaymentStatus() != null){
            existingBooking.setPaymentStatus(bookingDTO.getPaymentStatus());
        }

        bookingRepository.save(existingBooking);

        return Response.builder()
                .status(200)
                .message("Successfully Updated Booking")
                .booking(bookingDTO)
                .build();

    }




    private BigDecimal calculateTotalPrice(Room room, BookingDTO bookingDTO) {
        BigDecimal pricePerNight = room.getPricePerNight();
        long days = ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());
        return pricePerNight.multiply(BigDecimal.valueOf(days));
    }



}

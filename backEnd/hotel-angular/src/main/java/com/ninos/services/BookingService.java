package com.ninos.services;

import com.ninos.dtos.BookingDTO;
import com.ninos.dtos.Response;

public interface BookingService {

    Response getAllBookings();
    Response createBooking(BookingDTO bookingDTO);
    Response findBookingByReferenceNo(String bookingReference);
    Response updateBooking(BookingDTO bookingDTO);

}

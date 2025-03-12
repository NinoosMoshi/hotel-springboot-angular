package com.ninos.services;

import com.ninos.entities.BookingReference;
import com.ninos.repositories.BookingReferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Random;


@RequiredArgsConstructor
@Service
public class BookingCodeGenerator {

    private final BookingReferenceRepository bookingReferenceRepository;


    public String generateBookingReference() {
        String bookingReference;

        // keep generating until a unique code is found
        do {
            bookingReference = generateRandomAlphaNumericCode(10); // generate code of length 10
        }while (isBookingReferenceExist(bookingReference));  // check if the code already exist. if it doesn't, exit

        saveBookingReferenceToDatabase(bookingReference);  // save to database

        return bookingReference;
    }


    private String generateRandomAlphaNumericCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPWTDYUBECYZ123456789";

        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }


    private boolean isBookingReferenceExist(String bookingReference) {
        return bookingReferenceRepository.findByReferenceNo(bookingReference).isPresent();
    }

    private void saveBookingReferenceToDatabase(String bookingReference) {
        BookingReference newBookingReference = BookingReference.builder()
                .referenceNo(bookingReference)
                .build();
        bookingReferenceRepository.save(newBookingReference);
    }

}
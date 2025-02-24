package com.ninos.repositories;

import com.ninos.entities.BookingReference;
import com.ninos.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingReferenceRepository extends JpaRepository<BookingReference, Long> {

    Optional<BookingReference> findByReferenceNo(String referenceNo);
}

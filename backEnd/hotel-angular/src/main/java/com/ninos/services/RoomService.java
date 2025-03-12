package com.ninos.services;

import com.ninos.dtos.Response;
import com.ninos.dtos.RoomDTO;
import com.ninos.enums.RoomType;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    Response addRoom(RoomDTO roomDTO, MultipartFile imageFile);
    Response updateRoom(RoomDTO roomDTO, MultipartFile imageFile);
    Response getAllRooms();
    Response getRoomById(Long id);
    Response deleteRoom(Long id);
    Response getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType);
    List<RoomType> getAllRoomTypes();
    Response searchRoom(String input);

}

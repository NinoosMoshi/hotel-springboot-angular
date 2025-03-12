package com.ninos.services.impl;

import com.ninos.dtos.Response;
import com.ninos.dtos.RoomDTO;
import com.ninos.entities.Room;
import com.ninos.enums.RoomType;
import com.ninos.exceptions.InvalidBookingStateAndDateException;
import com.ninos.exceptions.NotFoundException;
import com.ninos.repositories.RoomRepository;
import com.ninos.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image/";


    @Override
    public Response addRoom(RoomDTO roomDTO, MultipartFile imageFile) {
        Room roomToSave = modelMapper.map(roomDTO, Room.class);
        if(imageFile != null) {
            String imagePath = saveImage(imageFile);
            roomToSave.setImageUrl(imagePath);
        }

        roomRepository.save(roomToSave);

        return Response.builder()
                .status(200)
                .message("Successfully added room")
                .build();
    }

    @Override
    public Response updateRoom(RoomDTO roomDTO, MultipartFile imageFile) {
        Room existingRoom = roomRepository.findById(roomDTO.getId())
                .orElseThrow(() -> new NotFoundException("Room not found"));

        if(imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            existingRoom.setImageUrl(imagePath);
        }

        if(roomDTO.getRoomNumber() != null && roomDTO.getPricePerNight().compareTo(BigDecimal.ZERO) >= 0) {
            existingRoom.setPricePerNight(roomDTO.getPricePerNight());
        }

        if(roomDTO.getCapacity() != null && roomDTO.getCapacity() > 0) {
            existingRoom.setCapacity(roomDTO.getCapacity());
        }

        if(roomDTO.getType() != null) {
            existingRoom.setType(roomDTO.getType());
        }

        if (roomDTO.getDescription() != null) {
            existingRoom.setDescription(roomDTO.getDescription());
        }

        roomRepository.save(existingRoom);

        return Response.builder()
                .status(200)
                .message("Successfully updated room")
                .build();
    }

    @Override
    public Response getAllRooms() {
        List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<RoomDTO> roomDTOList = modelMapper.map(roomList, new TypeToken<List<RoomDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("Successfully Getting All Rooms")
                .rooms(roomDTOList)
                .build();

    }

    @Override
    public Response getRoomById(Long id) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        RoomDTO roomDTO = modelMapper.map(existingRoom, RoomDTO.class);

        return Response.builder()
                .status(200)
                .message("Successfully Getting All Rooms")
                .room(roomDTO)
                .build();
    }

    @Override
    public Response deleteRoom(Long id) {
        if(!roomRepository.existsById(id)) {
            throw new NotFoundException("Room not found");
        }
        roomRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Successfully Delete A Room")
                .build();
    }

    @Override
    public Response getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {

        if(checkInDate.isBefore(LocalDate.now())) {
            throw new InvalidBookingStateAndDateException("CheckInDate cannot be before today date");
        }
        if(checkOutDate.isBefore(checkInDate)) {
            throw new InvalidBookingStateAndDateException("CheckOutDate cannot be before checkInDate");
        }
        if(checkInDate.isEqual(checkOutDate)) {
            throw new InvalidBookingStateAndDateException("CheckInDate cannot be equal to checkOutDate");
        }

        List<Room> roomList = roomRepository.findAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomDTO> roomDTOList = modelMapper.map(roomList, new TypeToken<List<RoomDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("Successfully Getting Available Rooms")
                .rooms(roomDTOList)
                .build();
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return Arrays.asList(RoomType.values());
    }

    @Override
    public Response searchRoom(String input) {
        List<Room> roomList = roomRepository.searchRooms(input);
        List<RoomDTO> roomDTOList = modelMapper.map(roomList, new TypeToken<List<RoomDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("Successfully Searching For Rooms")
                .rooms(roomDTOList)
                .build();
    }


    /** SAVE IMAGES TO DIRECTORY **/
    private String saveImage(MultipartFile imageFile) {
        if (!imageFile.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // create directory to store image if it does not exist
        File directory = new File(IMAGE_DIRECTORY);

        if(!directory.exists()){
            directory.mkdir();
        }

        // generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        // get the absolute path of the image
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }

        return imagePath;
    }





}


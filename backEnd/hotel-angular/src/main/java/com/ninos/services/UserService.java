package com.ninos.services;

import com.ninos.dtos.LoginRequest;
import com.ninos.dtos.RegisterRequest;
import com.ninos.dtos.Response;
import com.ninos.dtos.UserDTO;
import com.ninos.entities.User;

public interface UserService {

    Response registerUser(RegisterRequest registerRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    Response getOwnAccountDetails();
    User getCurrentLoggedInUser();
    Response updateOwnAccount(UserDTO userDTO);
    Response deleteOwnAccount();
    Response getMyBookingHistory();


}
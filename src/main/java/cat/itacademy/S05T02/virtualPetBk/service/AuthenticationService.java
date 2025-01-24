package cat.itacademy.S05T02.virtualPetBk.service;

import cat.itacademy.S05T02.virtualPetBk.dto.LoginUserDto;
import cat.itacademy.S05T02.virtualPetBk.dto.RegisterUserDto;
import cat.itacademy.S05T02.virtualPetBk.model.User;

public interface AuthenticationService {
    User signup(RegisterUserDto registerUser);
    User login(LoginUserDto loginUser);
}

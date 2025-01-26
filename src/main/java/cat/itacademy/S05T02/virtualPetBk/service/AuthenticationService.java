package cat.itacademy.S05T02.virtualPetBk.service;

import cat.itacademy.S05T02.virtualPetBk.dto.LoginUserDto;
import cat.itacademy.S05T02.virtualPetBk.dto.RegisterUserDto;
import cat.itacademy.S05T02.virtualPetBk.model.User;

import java.util.Optional;

public interface AuthenticationService {
    User signup(RegisterUserDto registerUser);
    User login(LoginUserDto loginUser);
    Optional<User> findByUsername(String userName);
}

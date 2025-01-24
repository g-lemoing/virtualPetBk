package cat.itacademy.S05T02.virtualPetBk.controller;

import cat.itacademy.S05T02.virtualPetBk.dto.LoginResponseDto;
import cat.itacademy.S05T02.virtualPetBk.dto.LoginUserDto;
import cat.itacademy.S05T02.virtualPetBk.dto.RegisterUserDto;
import cat.itacademy.S05T02.virtualPetBk.model.User;
import cat.itacademy.S05T02.virtualPetBk.service.AuthenticationService;
import cat.itacademy.S05T02.virtualPetBk.service.JwtServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtServiceImpl jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtServiceImpl jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.login(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}

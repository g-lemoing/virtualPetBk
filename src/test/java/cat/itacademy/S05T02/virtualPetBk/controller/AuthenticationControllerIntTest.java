package cat.itacademy.S05T02.virtualPetBk.controller;

import cat.itacademy.S05T02.virtualPetBk.dto.LoginResponseDto;
import cat.itacademy.S05T02.virtualPetBk.dto.LoginUserDto;
import cat.itacademy.S05T02.virtualPetBk.dto.RegisterUserDto;
import cat.itacademy.S05T02.virtualPetBk.model.Role;
import cat.itacademy.S05T02.virtualPetBk.model.User;
import cat.itacademy.S05T02.virtualPetBk.repository.UserRepository;
import cat.itacademy.S05T02.virtualPetBk.service.JwtServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerIntTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RegisterUserDto registerUserDto;
    private User user;
    private LoginUserDto loginUserDto;

    @BeforeEach
    void setup(){
        registerUserDto = new RegisterUserDto();
        registerUserDto.setUserName("user1");
        registerUserDto.setPassword("password1");
        user = new User(1, registerUserDto.getUserName(), Role.ROLE_USER);
        loginUserDto = new LoginUserDto();
        loginUserDto.setUserName("user1");
        loginUserDto.setPassword("user1");
    }

    @Test
    @Order(1)
    void registerUserOKTest(){
        when(userRepository.findByUserName(registerUserDto.getUserName())).thenReturn(List.of());
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        ResponseEntity<User> userResponseEntity = restTemplate.postForEntity(
                "/auth/signup", registerUserDto, User.class);
        assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(userResponseEntity.getBody());
        assertThat(Objects.equals(userResponseEntity.getBody().getUsername(), user.getUsername()))
                .isTrue();
        verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    @Order(2)
    void registerUserAlreadyExistsKoTest(){
        when(userRepository.findByUserName(registerUserDto.getUserName())).thenReturn(List.of(user));
        ResponseEntity<ProblemDetail> responseEntity = restTemplate.postForEntity(
                "/auth/signup", registerUserDto, ProblemDetail.class);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("User already exists, please use another user name.",
                Objects.requireNonNull(responseEntity.getBody().getProperties()).get("description"));
        verify(userRepository, never()).save(ArgumentMatchers.any(User.class));
    }

    @Test
    @Order(3)
    void loginUserWhenCredentialsCorrectThenGenerateTokenOk(){
        user.setPassword(passwordEncoder.encode(loginUserDto.getPassword()));
        when(userRepository.findByUserName(loginUserDto.getUserName())).thenReturn(List.of(user));

        ResponseEntity<LoginResponseDto> responseEntity = restTemplate.postForEntity(
                "/auth/login", loginUserDto, LoginResponseDto.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
        String token = responseEntity.getBody().getToken();
        assertNotNull(token);

        String userName = jwtService.extractUserName(token);
        Long expirationTime = jwtService.getExpirationTime();
        assertEquals(userName, loginUserDto.getUserName());
        assertEquals(3600000, expirationTime);
        verify(authenticationManager, times(1))
                .authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @Order(4)
    void loginUserWhenCredentialsNotCorrectThrowsExceptionKo(){
        loginUserDto.setPassword("incorrectPassword");
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        user.setPassword(passwordEncoder.encode(loginUserDto.getPassword()));

        when(userRepository.findByUserName(loginUserDto.getUserName())).thenReturn(List.of(user));

        ResponseEntity<ProblemDetail> responseEntity = restTemplate.postForEntity(
                "/auth/login", loginUserDto, ProblemDetail.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("The username or password is incorrect",
                Objects.requireNonNull(responseEntity.getBody().getProperties()).get("description"));
        verify(authenticationManager, times(1))
                .authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class));
    }
}

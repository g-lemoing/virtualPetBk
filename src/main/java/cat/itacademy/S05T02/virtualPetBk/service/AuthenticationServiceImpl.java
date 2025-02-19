package cat.itacademy.S05T02.virtualPetBk.service;

import cat.itacademy.S05T02.virtualPetBk.dto.LoginUserDto;
import cat.itacademy.S05T02.virtualPetBk.dto.RegisterUserDto;
import cat.itacademy.S05T02.virtualPetBk.exception.UserNameAlreadyExistsException;
import cat.itacademy.S05T02.virtualPetBk.exception.UserNotFoundException;
import cat.itacademy.S05T02.virtualPetBk.model.Role;
import cat.itacademy.S05T02.virtualPetBk.model.User;
import cat.itacademy.S05T02.virtualPetBk.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User signup(RegisterUserDto registerUser){
        String submittedName = registerUser.getUserName();
        if(!findByUsername(submittedName).isEmpty()) throw new UserNameAlreadyExistsException(submittedName);

        User user = new User();
        user.setUserName(submittedName);
        user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }

    @Override
    public User login(LoginUserDto loginUser) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUserName(),
                        loginUser.getPassword())
        );
        return userRepository.findByUserName(loginUser.getUserName())
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(loginUser.getUserName()));
    }

    @Override
    public List<User> findByUsername(String userName) {
        List<User> users = userRepository.findByUserName(userName);
        return users;
    }
}

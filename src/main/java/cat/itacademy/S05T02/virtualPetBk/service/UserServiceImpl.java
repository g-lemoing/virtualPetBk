package cat.itacademy.S05T02.virtualPetBk.service;

import cat.itacademy.S05T02.virtualPetBk.model.User;
import cat.itacademy.S05T02.virtualPetBk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }
}

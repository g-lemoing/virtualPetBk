package cat.itacademy.S05T02.virtualPetBk.service;

import cat.itacademy.S05T02.virtualPetBk.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
}

package cat.itacademy.S05T02.virtualPetBk.service;

import cat.itacademy.S05T02.virtualPetBk.dto.UserPetCreateDto;
import cat.itacademy.S05T02.virtualPetBk.model.UserPet;

import java.util.List;

public interface PetService {
    List<UserPet> getAllPets();
    UserPet createUserPet(UserPetCreateDto userPetCreateDto);
    UserPet updateUserPet(int petUserId, String action);
    void deleteUserPet(int petUserId);
}

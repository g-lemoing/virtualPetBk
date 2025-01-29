package cat.itacademy.S05T02.virtualPetBk.service;

import cat.itacademy.S05T02.virtualPetBk.dto.UserPetCreateDto;
import cat.itacademy.S05T02.virtualPetBk.model.Action;
import cat.itacademy.S05T02.virtualPetBk.model.UserPet;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PetService {
    List<UserPet> getAllUserPets(UserDetails userDetails);
    List<UserPet> getAllUsersPets();
    UserPet createUserPet(UserPetCreateDto userPetCreateDto);
    UserPet updateUserPet(UserDetails userDetails, int petUserId, Action action);
}

package cat.itacademy.S05T02.virtualPetBk.service;

import cat.itacademy.S05T02.virtualPetBk.dto.UserPetCreateDto;
import cat.itacademy.S05T02.virtualPetBk.exception.PetNameAlreadyExistsException;
import cat.itacademy.S05T02.virtualPetBk.exception.UserNotFoundException;
import cat.itacademy.S05T02.virtualPetBk.model.Action;
import cat.itacademy.S05T02.virtualPetBk.model.Animal;
import cat.itacademy.S05T02.virtualPetBk.model.PetColor;
import cat.itacademy.S05T02.virtualPetBk.model.UserPet;
import cat.itacademy.S05T02.virtualPetBk.repository.UserPetRepository;
import cat.itacademy.S05T02.virtualPetBk.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetServiceImpl implements PetService{
    private UserPetRepository userPetRepository;
    private UserRepository userRepository;

    public PetServiceImpl(UserPetRepository userPetRepository, UserRepository userRepository) {
        this.userPetRepository = userPetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserPet> getAllUsersPets() {
        return userPetRepository.findAll();
    }

    @Override
    public List<UserPet> getAllUserPets(UserDetails userDetails) {
        return List.of();
    }

    @Override
    public UserPet createUserPet(UserPetCreateDto userPetCreateDto) {
        int userId = userPetCreateDto.getUserId();
        String petName = userPetCreateDto.getPetName();
        if(userRepository.findById(userId).isEmpty())
            throw new UserNotFoundException("");

        if (!userPetRepository.findByUserIdAndPetName(userId, petName).isEmpty())
            throw new PetNameAlreadyExistsException(petName, userId);

        UserPet userPet = createNewUserPet(userPetCreateDto);
        return userPetRepository.save(userPet);
    }

    @Override
    public UserPet updateUserPet(UserDetails userDetails, int petUserId, Action action) {
        return null;
    }

    private UserPet createNewUserPet(UserPetCreateDto userPetCreateDto){
        int userId = userPetCreateDto.getUserId();
        Animal animal = userPetCreateDto.getAnimal();
        String petName = userPetCreateDto.getPetName();
        PetColor petColor = userPetCreateDto.getPetColor();
        return new UserPet(userId, animal, petName, petColor,
                0.5, 0.5, 0.5);
    }
}

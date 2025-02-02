package cat.itacademy.S05T02.virtualPetBk.service;

import cat.itacademy.S05T02.virtualPetBk.dto.UserPetCreateDto;
import cat.itacademy.S05T02.virtualPetBk.exception.PetNameAlreadyExistsException;
import cat.itacademy.S05T02.virtualPetBk.exception.PetNotFoundException;
import cat.itacademy.S05T02.virtualPetBk.exception.UserNotFoundException;
import cat.itacademy.S05T02.virtualPetBk.model.*;
import cat.itacademy.S05T02.virtualPetBk.repository.UserPetRepository;
import cat.itacademy.S05T02.virtualPetBk.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetServiceImpl implements PetService{
    private final UserPetRepository userPetRepository;
    private final UserRepository userRepository;

    public PetServiceImpl(UserPetRepository userPetRepository, UserRepository userRepository) {
        this.userPetRepository = userPetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserPet> getAllPets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Object principal = authentication.getPrincipal();

        if(!(principal instanceof User userDetails)) throw new UserNotFoundException(userName);

        int userId = userDetails.getId();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return roles.contains("ROLE_ADMIN") ? userPetRepository.findAll() : userPetRepository.findAllUserPetsByUserId(userId);
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
    public UserPet updateUserPet(int petUserId, String actionStr) {
        UserPet userPet = getUserPet(petUserId);
        Action action = Action.valueOf(actionStr.toUpperCase());
        return updatePetLevels(userPet, action);
    }

    @Override
    public void deleteUserPet(int petUserId) {
        userPetRepository.delete(getUserPet(petUserId));
    }

    protected UserPet createNewUserPet(@NotNull UserPetCreateDto userPetCreateDto){
        int userId = userPetCreateDto.getUserId();
        Animal animal = userPetCreateDto.getAnimal();
        String petName = userPetCreateDto.getPetName();
        PetColor petColor = userPetCreateDto.getPetColor();
        return new UserPet(userId, animal, petName, petColor,
                0.5, 0.5, 0.5);
    }

    protected UserPet getUserPet(int petUserId){
        return userPetRepository.findById(petUserId)
                .orElseThrow(() -> new PetNotFoundException(petUserId));
    }

    protected UserPet updatePetLevels(UserPet userPet, Action action){
        double petMood = userPet.getPetMood();
        double petHungryLevel = userPet.getPetHungryLevel();
        double petEnergyLevel = userPet.getPetEnergyLevel();

        switch (action){
            case FEED ->{petMood += 0.1; petHungryLevel = 0.0; petEnergyLevel += 0.3;}
            case PLAY -> {petMood += 0.2; petHungryLevel += 0.2; petEnergyLevel -= 0.2;}
            case WORK -> {petMood -= 0.3; petHungryLevel += 0.3; petEnergyLevel -= 0.4;}
            case SLEEP -> {petMood += 0.5; petEnergyLevel = 1.0;}
            case SUNGLASSES -> {petMood += 0.2; petEnergyLevel += 0.1;}
            case WALKRAIN -> {petMood -= 0.4; petHungryLevel += 0.1; petEnergyLevel -= 0.1;}
            case BEACH -> {petMood = 1.0; petHungryLevel += 0.2; petEnergyLevel += 0.5;}
            case null, default -> throw new IllegalArgumentException();
        }

        userPet.setPetMood(Math.min(Math.max(0.0, petMood), 1.0));
        userPet.setPetHungryLevel(Math.min(Math.max(0.0, petHungryLevel), 1.0));
        userPet.setPetEnergyLevel(Math.min(Math.max(0.0, petEnergyLevel), 1.0));
        return userPetRepository.save(userPet);
    }
}

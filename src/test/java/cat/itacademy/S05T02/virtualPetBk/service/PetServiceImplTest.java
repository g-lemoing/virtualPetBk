package cat.itacademy.S05T02.virtualPetBk.service;

import cat.itacademy.S05T02.virtualPetBk.dto.UserPetCreateDto;
import cat.itacademy.S05T02.virtualPetBk.exception.PetNameAlreadyExistsException;
import cat.itacademy.S05T02.virtualPetBk.exception.PetNotFoundException;
import cat.itacademy.S05T02.virtualPetBk.exception.UserNotFoundException;
import cat.itacademy.S05T02.virtualPetBk.model.*;
import cat.itacademy.S05T02.virtualPetBk.repository.UserPetRepository;
import cat.itacademy.S05T02.virtualPetBk.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private UserPetRepository userPetRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private PetServiceImpl petService;

    private UserPet userPet1;
    private UserPet userPet2;
    private UserPet userPet3;
    private User userAdmin;
    private User userNoAdmin;
    private List<UserPet> userPetWholeList;
    private List<UserPet> userPetList;

    @BeforeEach
    void initTests(){
        userPet1 = new UserPet(97, Animal.MONKEY, "Pet1",
                PetColor.BLUE, 0.5, 0.5, 0.5);
        userPet2 = new UserPet(98, Animal.MONKEY, "Pet2",
                PetColor.BLUE, 0.1, 0.9, 0.5);
        userPet3 = new UserPet(98, Animal.MONKEY, "Pet3",
                PetColor.BLUE, 0.5, 0.5, 0.5);
        userAdmin = new User(97, "userAdmin", Role.ROLE_ADMIN);
        userNoAdmin = new User(98,"userNoAdmin", Role.ROLE_USER);
        userPetWholeList = List.of(userPet1, userPet2, userPet3);
        userPetList = List.of(userPet2, userPet3);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllPetsWhenRoleIsAdmin() {

        User userDetails = userAdmin;
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("admin");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.getAuthorities()).thenAnswer(unused -> Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(userPetRepository.findAll()).thenReturn(userPetWholeList);
        List<UserPet> userPets = petService.getAllPets();
        assertEquals(userPetWholeList, userPets);
        verify(userPetRepository, times(1)).findAll();
        verify(userPetRepository, times(0)).findAllUserPetsByUserId(97);
    }

    @Test
    void getAllUserPetsWhenRoleIsUser() {
        User userDetails = userNoAdmin;
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.getAuthorities()).thenAnswer(unused -> Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(userPetRepository.findAllUserPetsByUserId(98)).thenReturn(userPetList);

        List<UserPet> userPets = petService.getAllPets();
        assertEquals(userPets, userPetList);
        verify(userPetRepository, times(0)).findAll();
        verify(userPetRepository, times(1)).findAllUserPetsByUserId(98);
    }
    @Test
    void createUserPetKoWhenUserNotExists() {
        UserPetCreateDto userPetCreateDto = new UserPetCreateDto(99, Animal.MONKEY, "Pet2", PetColor.GREEN);

        when(userRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, ()-> petService.createUserPet(userPetCreateDto));
    }

    @Test
    void createUserPetKoWhenPetNameAlreadyExistsForUser() {
        UserPetCreateDto userPetCreateDto = new UserPetCreateDto(userNoAdmin.getId(), Animal.MONKEY, "Pet2", PetColor.GREEN);

        when(userRepository.findById(userNoAdmin.getId())).thenReturn(Optional.of(userNoAdmin));
        when(userPetRepository.findByUserIdAndPetName(userNoAdmin.getId(), userPetCreateDto.getPetName())).thenReturn(List.of(userPet2));

        assertThrows(PetNameAlreadyExistsException.class, ()-> petService.createUserPet(userPetCreateDto));
        verify(userRepository, times(1)).findById(userNoAdmin.getId());
        verify(userPetRepository, times(1)).findByUserIdAndPetName(userNoAdmin.getId(), userPetCreateDto.getPetName());
    }

    @Test
    void createUserPetOkWhenPetNameIsNewAndUserExists() {
        UserPetCreateDto userPetCreateDto = new UserPetCreateDto(userNoAdmin.getId(), Animal.MONKEY, "Pet4", PetColor.GREEN);
        UserPet newUserPet = new UserPet(
                userNoAdmin.getId(), userPetCreateDto.getAnimal(), userPetCreateDto.getPetName(),
                userPetCreateDto.getPetColor(),0.5, 0.5, 0.5);

        when(userRepository.findById(userNoAdmin.getId())).thenReturn(Optional.of(userNoAdmin));
        when(userPetRepository.findByUserIdAndPetName(userNoAdmin.getId(), userPetCreateDto.getPetName())).thenReturn(List.of());
        when(userPetRepository.save(any(UserPet.class))).thenReturn(newUserPet);

        UserPet userPetResult = petService.createUserPet(userPetCreateDto);

        assertEquals(userPetResult, newUserPet);
        verify(userRepository, times(1)).findById(userNoAdmin.getId());
        verify(userPetRepository, times(1)).findByUserIdAndPetName(userNoAdmin.getId(), userPetCreateDto.getPetName());
        verify(userPetRepository, times(1)).save(any(UserPet.class));
    }

    @Test
    void updateUserPetWhenActionExists() {
        String action = "Play";
        UserPet expectedUserPet = new UserPet(98, Animal.MONKEY, "Pet2",
                PetColor.BLUE, 0.01, 1.0, 0.8);
        when(userPetRepository.findById(1)).thenReturn(Optional.of(userPet2));
        when(userPetRepository.save(ArgumentMatchers.any(UserPet.class))).thenAnswer(answer -> answer.getArgument(0));
        UserPet userPetResult = petService.updateUserPet(1, action);
        assertAll(
                () -> assertEquals(expectedUserPet.getPetUserId(), userPetResult.getPetUserId()),
                () -> assertEquals(expectedUserPet.getPetMood(), userPetResult.getPetMood()),
                () -> assertEquals(expectedUserPet.getPetHungryLevel(), userPetResult.getPetHungryLevel()),
                () -> assertEquals(userPetResult.getPetEnergyLevel(), expectedUserPet.getPetEnergyLevel())
        );
        verify(userPetRepository, times(1)).findById(1);
        verify(userPetRepository, times(1)).save(ArgumentMatchers.any(UserPet.class));
    }

    @Test
    void deleteUserPetWhenUserPetExists() {
        when(userPetRepository.findById(1)).thenReturn(Optional.of(userPet2));
        assertDoesNotThrow(() -> petService.deleteUserPet(1));
        verify(userPetRepository, times(1)).delete(userPet2);
        verify(userPetRepository, times(1)).findById(1);
    }

    @Test
    void deleteUserPetWhenUserPetNotExists() {
        when(userPetRepository.findById(11)).thenThrow(new PetNotFoundException(11));
        assertThrows(PetNotFoundException.class, () -> petService.deleteUserPet(11));
        verify(userPetRepository, times(0)).delete(userPet2);
        verify(userPetRepository, times(1)).findById(11);
    }

    @Test
    void updatePetLevelsTestWithinBounds(){
        UserPet expectedUserPet = new UserPet(97, Animal.MONKEY, "Pet1",
                PetColor.BLUE, 0.3, 0.7, 0.8);
        when(userPetRepository.save(userPet1)).thenReturn(userPet1);
        UserPet updatedUserPet = petService.updatePetLevels(userPet1, Action.PLAY);
        assertAll(
                () -> assertEquals(expectedUserPet.getPetEnergyLevel(), updatedUserPet.getPetEnergyLevel(), "PetEnergyLevel should be 0.3"),
                () -> assertEquals(expectedUserPet.getPetMood(), updatedUserPet.getPetMood(), "PetMood should be 0.7"),
                () -> assertEquals(expectedUserPet.getPetHungryLevel(), updatedUserPet.getPetHungryLevel(), "PetHungryLevel should be 0.8")
        );
    }

    @Test
    void updatePetLevelsTestWhenOutOfBounds(){
        UserPet expectedUserPet = new UserPet(97, Animal.MONKEY, "Pet1",
                PetColor.BLUE, 0.01, 1.0, 0.8);
        when(userPetRepository.save(userPet2)).thenReturn(userPet2);
        UserPet updatedUserPet = petService.updatePetLevels(userPet2, Action.PLAY);
        assertAll(
                () -> assertEquals(expectedUserPet.getPetEnergyLevel(), updatedUserPet.getPetEnergyLevel(), "PetEnergyLevel should be 0.01"),
                () -> assertEquals(expectedUserPet.getPetMood(), updatedUserPet.getPetMood(), "PetMood should be 1.0"),
                () -> assertEquals(expectedUserPet.getPetHungryLevel(), updatedUserPet.getPetHungryLevel(), "PetHungryLevel should be 0.7")
        );
    }}


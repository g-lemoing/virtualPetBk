package cat.itacademy.S05T02.virtualPetBk.controller;

import cat.itacademy.S05T02.virtualPetBk.dto.UserPetCreateDto;
import cat.itacademy.S05T02.virtualPetBk.exception.PetNameAlreadyExistsException;
import cat.itacademy.S05T02.virtualPetBk.exception.PetNotFoundException;
import cat.itacademy.S05T02.virtualPetBk.model.*;
import cat.itacademy.S05T02.virtualPetBk.service.PetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    PetServiceImpl petService;
    @Mock
    Authentication authentication;

    @InjectMocks
    PetController petController;

    private UserPetCreateDto userPetCreateDto;
    private UserPet userPet1;
    private UserPet userPet2;
    private UserPet userPet3;
    private List<UserPet> userPetWholeList;
    private List<UserPet> userPetList;

    @BeforeEach
    void setUp() {
        userPetCreateDto = new UserPetCreateDto(1, Animal.MONKEY, "Duna", PetColor.GREEN);
        userPet1 = new UserPet(1, Animal.MONKEY, "Duna", PetColor.GREEN,
                0.5, 0.5, 0.5);
        userPet2 = new UserPet(98, Animal.MONKEY, "Pet2",
                PetColor.BLUE, 0.1, 0.9, 0.5);
        userPet3 = new UserPet(98, Animal.MONKEY, "Pet3",
                PetColor.BLUE, 0.5, 0.5, 0.5);
        User userAdmin = new User(97, "userAdmin", Role.ROLE_ADMIN);
        User userNoAdmin = new User(98,"userNoAdmin", Role.ROLE_USER);
        userPetWholeList = List.of(userPet1, userPet2, userPet3);
        userPetList = List.of(userPet2, userPet3);
    }

    @Test
    void createUserPetShouldReturn200WhenValidRequest() throws Exception {
        when(petService.createUserPet(userPetCreateDto)).thenReturn(userPet1);
        ResponseEntity<UserPet> responseEntity = petController.createUserPet(userPetCreateDto, authentication);

        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(201));
        assertEquals(responseEntity.getBody(), userPet1);
        verify(petService, times(1)).createUserPet(userPetCreateDto);
    }

    @Test
    void createUserPetShouldReturn409WhenPetAlreadyExists() throws Exception {
        when(petService.createUserPet(userPetCreateDto))
                .thenThrow(new PetNameAlreadyExistsException(userPetCreateDto.getPetName(), userPetCreateDto.getUserId()));

        assertThrows(PetNameAlreadyExistsException.class, ()-> petController.createUserPet(userPetCreateDto, authentication));
        verify(petService, times(1)).createUserPet(userPetCreateDto);
    }

    @Test
    void createUserPetShouldReturn400WhenDataProvidedIsTruncatedInDatabase() throws Exception {
        when(petService.createUserPet(userPetCreateDto))
                .thenThrow(new DataIntegrityViolationException("Possible cause"));

        assertThrows(DataIntegrityViolationException.class, ()-> petController.createUserPet(userPetCreateDto, authentication));
        verify(petService, times(1)).createUserPet(userPetCreateDto);
    }

    @Test
    void getAllPetsWhenUserRoleIsAdmin(){
        when(petService.getAllPets()).thenReturn(userPetWholeList);
        ResponseEntity<List<UserPet>> responseEntity = petController.getAllPets(authentication);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        assertEquals(responseEntity.getBody(), userPetWholeList);
        verify(petService, times(1)).getAllPets();
    }

    @Test
    void getOwnerPetsWhenUserRoleIsUser(){
        when(petService.getAllPets()).thenReturn(userPetList);
        ResponseEntity<List<UserPet>> responseEntity = petController.getAllPets(authentication);
        assertEquals(responseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        assertEquals(responseEntity.getBody(), userPetList);
        verify(petService, times(1)).getAllPets();
    }

    @Test
    void deleteUserPetOkWhenUserPetExists(){
        doNothing().when(petService).deleteUserPet(ArgumentMatchers.anyInt());
        ResponseEntity<Void> responseEntity = petController.deletePet(authentication, 1);
        assertEquals(ResponseEntity.noContent().build(), responseEntity);
        verify(petService, times(1)).deleteUserPet(1);
    }

    @Test
    void deleteUserPetKoWhenUserPetNotExists(){
        int petUserId = 99;
        doThrow(new PetNotFoundException(petUserId)).when(petService).deleteUserPet(petUserId);
        assertThrows(PetNotFoundException.class, ()-> petController.deletePet(authentication, 99));
        verify(petService, times(1)).deleteUserPet(petUserId);
    }
}
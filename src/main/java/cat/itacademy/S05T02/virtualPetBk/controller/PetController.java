package cat.itacademy.S05T02.virtualPetBk.controller;

import cat.itacademy.S05T02.virtualPetBk.dto.UserPetCreateDto;
import cat.itacademy.S05T02.virtualPetBk.model.UserPet;
import cat.itacademy.S05T02.virtualPetBk.service.PetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private PetServiceImpl petService;

    @PostMapping("/create")
    ResponseEntity<UserPet> createUserPet(@RequestBody UserPetCreateDto userPetCreateDto,
                                          AuthenticationController authenticationController){
        UserPet userPet = petService.createUserPet(userPetCreateDto);
        return ResponseEntity.ok(userPet);
    }
}

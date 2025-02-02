package cat.itacademy.S05T02.virtualPetBk.controller;

import cat.itacademy.S05T02.virtualPetBk.dto.UserPetCreateDto;
import cat.itacademy.S05T02.virtualPetBk.model.UserPet;
import cat.itacademy.S05T02.virtualPetBk.service.PetServiceImpl;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/read")
    ResponseEntity<List<UserPet>> getAllPets(AuthenticationController authenticationController){
        List<UserPet> userPets = petService.getAllPets();
        return ResponseEntity.ok(userPets);
    }

    @PutMapping("/update/{petUserId}/{action}")
    ResponseEntity<UserPet> updatePet(AuthenticationController authenticationController,
                                      @PathVariable @Required int petUserId,
                                      @PathVariable @Required String action){
        UserPet userPet = petService.updateUserPet(petUserId, action);
        return ResponseEntity.ok(userPet);
    }

    @DeleteMapping("/delete/{petUserId}")
    ResponseEntity<Void> deletePet(AuthenticationController authenticationController,
                                   @PathVariable @Required int petUserId){
        petService.deleteUserPet(petUserId);
        return ResponseEntity.noContent().build();
    }
}

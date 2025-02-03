package cat.itacademy.S05T02.virtualPetBk.controller;

import cat.itacademy.S05T02.virtualPetBk.dto.UserPetCreateDto;
import cat.itacademy.S05T02.virtualPetBk.model.UserPet;
import cat.itacademy.S05T02.virtualPetBk.service.PetServiceImpl;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private PetServiceImpl petService;

    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    @PostMapping("/create")
    ResponseEntity<UserPet> createUserPet(@RequestBody UserPetCreateDto userPetCreateDto,
                                          Authentication authentication){
        UserPet userPet = petService.createUserPet(userPetCreateDto);
        log.info("New user pet {} successfully stored in database.", userPet);
        return ResponseEntity.status(201).body(userPet);
    }

    @GetMapping("/read")
    ResponseEntity<List<UserPet>> getAllPets(Authentication authentication){
        List<UserPet> userPets = petService.getAllPets();
        log.info("User pet list retrieved from database: {}", userPets);
        return ResponseEntity.ok(userPets);
    }

    @PutMapping("/update/{petUserId}/{action}")
    ResponseEntity<UserPet> updatePet(Authentication authentication, @PathVariable @Required int petUserId,
                                      @PathVariable @Required String action){
        UserPet userPet = petService.updateUserPet(petUserId, action);
        log.info("Updated user pet: {}", userPet);
        return ResponseEntity.ok(userPet);
    }

    @DeleteMapping("/delete/{petUserId}")
    ResponseEntity<Void> deletePet(Authentication authentication, @PathVariable @Required int petUserId){
        petService.deleteUserPet(petUserId);
        log.info("Pet successfully removed with id {}.", petUserId);
        return ResponseEntity.noContent().build();
    }
}

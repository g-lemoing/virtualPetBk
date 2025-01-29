package cat.itacademy.S05T02.virtualPetBk.controller;

import cat.itacademy.S05T02.virtualPetBk.model.User;
import cat.itacademy.S05T02.virtualPetBk.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
}

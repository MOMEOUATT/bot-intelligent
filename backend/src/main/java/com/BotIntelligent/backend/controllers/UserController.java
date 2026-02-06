package com.BotIntelligent.backend.controllers;

import com.BotIntelligent.backend.dtos.UserDto;
import com.BotIntelligent.backend.entities.User;
import com.BotIntelligent.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto dto){
        try {
            User user = userService.createUser(dto.getEmail(), dto.getUsername(), dto.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDto dto){
        try {
            User updated =  userService.updateUser(id, dto.getEmail(), dto.getUsername(), dto.getPassword());
            return ResponseEntity.ok(updated);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
       List<User> users = userService.getAllUsers();
       return ResponseEntity.ok(users);
    }
}

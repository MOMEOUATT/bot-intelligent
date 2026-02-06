package com.BotIntelligent.backend.service;

import com.BotIntelligent.backend.entities.User;
import com.BotIntelligent.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String email, String username, String password){
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("Email déjà utilisé");
        }

        if(userRepository.existsByUsername(username)){
            throw new RuntimeException("Nom d'utilisateur déjà pris");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateUser(Long id, String email, String username, String password){
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Utilisateur non trouvé")
        );
        if(email != null){
            user.setEmail(email);
        }

        if(username != null){
            user.setUsername(username);
        }

        if(password != null){
            user.setPassword(password);
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}

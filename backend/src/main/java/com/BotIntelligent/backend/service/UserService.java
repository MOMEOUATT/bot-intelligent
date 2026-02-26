package com.BotIntelligent.backend.service;

import com.BotIntelligent.backend.entities.User;
import com.BotIntelligent.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(password));

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

    public User updateProfile(Long id, String email, String username) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));


        if(email != null || !email.trim().isEmpty()){
            Optional<User> existingEmail = userRepository.findByEmail(email.trim());
            if(existingEmail.isPresent()){
                throw new RuntimeException("Cet Email est déjà utilisé");
            }
            user.setEmail(email.trim());
        }

        if(username != null || !username.trim().isEmpty()){
            Optional<User> existingUsername = userRepository.findByUsername(username.trim());
            if(existingUsername.isPresent()){
                throw new RuntimeException("Ce nom d'utilisateur est déjà pris");
            }
            user.setUsername(username.trim());
        }

        return userRepository.save(user);
    }

    public User changePassword(Long id, String oldPassword, String newPassword){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new RuntimeException("Mot de passe actuel incorrect");
        }

        if(newPassword == null || newPassword.trim().isEmpty()){
            throw new RuntimeException("Le nouveau mot de passe doit contenir au moins 6 caractères");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
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

    public boolean verifyPassword(String email, String password){
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}

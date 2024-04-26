package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    // TODO: wire in the user repository (~ 1 line)
    private UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {
        // TODO: Create an user entity with information given in the payload, store it in the database
        //       and return the id of the user in 200 OK response
        //If user email has already exist
        Optional<User> findUser = userRepository.findByEmail(payload.getEmail());
        if (findUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User newUser = new User(payload.getName(), payload.getEmail());
        userRepository.save(newUser);

        return ResponseEntity.ok(newUser.getId());
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // TODO: Return 200 OK if a user with the given ID exists, and the deletion is successful
        //       Return 400 Bad Request if a user with the ID does not exist
        //       The response body could be anything you consider appropriate
        Optional<User> find = userRepository.findById(userId);
        if (find.isPresent()) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

    }
}

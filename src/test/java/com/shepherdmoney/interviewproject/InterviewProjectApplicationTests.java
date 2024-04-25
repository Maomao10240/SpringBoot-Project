package com.shepherdmoney.interviewproject;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class InterviewProjectApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testSaveUser() {
        // Mocking dependencies
        UserRepository userRepository = mock(UserRepository.class);
        User user = new User("Helen", "helen@gmail.com");

        // Stubbing save method to return the user object
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Testing save method
        User savedUser = userRepository.save(user);

        // Verifying that the save method was called with the correct argument
        verify(userRepository).save(user);

        // Asserting that the savedUser is the same as the user object
        assertEquals(user, savedUser);
    }

    @Test
    public void testFindUserById() {
        // Mocking dependencies
        UserRepository userRepository = mock(UserRepository.class);
        User user = new User("Helen", "helen@gmail.com");

        // Stubbing findById method to return an Optional containing the user object
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Testing findById method
        Optional<User> findUser = userRepository.findById(1);

        // Verifying that the findById method was called with the correct argument
        verify(userRepository).findById(1);

        // Asserting that the findUser Optional contains the user object
        assertEquals(user, findUser.get());
    }

}

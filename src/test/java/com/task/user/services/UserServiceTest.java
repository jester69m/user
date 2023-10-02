package com.task.user.services;


import com.task.user.exceptions.UserNotAcceptableAge;
import com.task.user.exceptions.UserNotFoundException;
import com.task.user.models.User;
import com.task.user.payloads.UserUpdateRequest;
import com.task.user.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("mail@mail.com");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setDateOfBirth(new Date(10L*365L*24L*60L*60L*1000L));
        user.setPhoneNumber("1234567890");
        user.setAddress("Some address");
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUserAndFindById() {
        userService.save(user);
        assertNotNull(userService.findById(1L));
    }

    @Test
    public void testThrowsUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.findById(2L));
    }

    @Test
    public void testUpdateUser() {
        userService.save(user);
        UserUpdateRequest user2 = new UserUpdateRequest();
        user2.setFirstName("New First");
        userService.update(user.getId(), user2);
        assertEquals("New First", userService.findById(user.getId()).getFirstName());
    }
    @Test
    public void testUpdateUserException() {
        userService.save(user);
        UserUpdateRequest user2 = new UserUpdateRequest();
        user2.setFirstName("");
        assertThrows(RuntimeException.class, () -> userService.update(user.getId(), user2));
        user2.setFirstName(null);
        user2.setEmail("1mail");
        assertThrows(RuntimeException.class, () -> userService.update(user.getId(), user2));
        user2.setEmail(null);
        user2.setDateOfBirth(new Date(15L * 365L * 24L * 60L * 60L * 1000L));
        assertDoesNotThrow( () -> userService.update(user.getId(), user2));

        user2.setDateOfBirth(new Date(50L * 365L * 24L * 60L * 60L * 1000L));
        assertThrows(UserNotAcceptableAge.class, () -> userService.update(user.getId(), user2));

    }

    @Test
    public void testDeleteUser() {
        userService.save(user);
        assertNotNull(userService.findById(user.getId()));
        userService.delete(user.getId());
        assertThrows(UserNotFoundException.class, () -> userService.findById(user.getId()));
    }

    @Test
    public void testFindUsersByDateOfBirthBetween() {
        userService.save(user);
        User user2 = new User();
        user2.setFirstName("First2");
        user2.setLastName("Last2");
        user2.setEmail("mail2@mail.com");
        user2.setDateOfBirth(new java.util.Date(20L*365L*24L*60L*60L*1000L));
        userService.save(user2);
        assertEquals(2, userService.findUsersByDateOfBirthBetween(
                new Date(0L),
                new Date(40L*365L*24L*60L*60L*1000L),
                PageRequest.of(0,10)).get().count());

        assertEquals(user, userService.findUsersByDateOfBirthBetween(
                 new Date(0L),
                new Date(40L*365L*24L*60L*60L*1000L),
                PageRequest.of(0,10)).get().toList().get(0));

        assertEquals(1, userService.findUsersByDateOfBirthBetween(
                new Date(0L),
                new Date(15L*365L*24L*60L*60L*1000L),
                PageRequest.of(0,10)).get().count());

        assertEquals(1,userService.findUsersByDateOfBirthBetween(
                new Date(15L*365L*24L*60L*60L*1000L),
                new Date(40L*365L*24L*60L*60L*1000L),
                PageRequest.of(0,10)).get().count());

        assertEquals(0,userService.findUsersByDateOfBirthBetween(
                new Date(30L*365L*24L*60L*60L*1000L),
                new Date(40L*365L*24L*60L*60L*1000L),
                PageRequest.of(0,10)).get().count());
    }

}

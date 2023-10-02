package com.task.user.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserModelTest {

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("some@mail.com");
        user.setFirstName("Some");
        user.setLastName("One");
        user.setDateOfBirth(new Date(10L*365L*24L*60L*60L*1000L));
        user.setPhoneNumber("1234567890");
        user.setAddress("Some address");

        assertNotNull(user);
        assertEquals("User(id=1, email=some@mail.com, firstName=Some, lastName=One," +
                " dateOfBirth=Sun Dec 30 03:00:00 EET 1979, " +
                "address=Some address, phoneNumber=1234567890)", user.toString());
    }
}

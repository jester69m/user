package com.task.user.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.user.models.User;
import com.task.user.payloads.UserUpdateRequest;
import com.task.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("mail@mail.com");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setDateOfBirth(new Date(10L*365L*24L*60L*60L*1000L));

    }

    @Test
    public void testCreateUser() throws Exception {
        when(userService.save(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());

        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser() throws Exception {

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("mail@mail.com");
        updatedUser.setFirstName("Changed First");
        updatedUser.setLastName("Last");
        updatedUser.setDateOfBirth(new Date(10L*365L*24L*60L*60L*1000L));

        when(userService.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value("Changed First"));
    }

    @Test
    public void testPatchUpdateUser() throws Exception {
        Long userId = 1L;

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setFirstName("Updated First Name");

        user.setId(userId);
        user.setFirstName(userUpdateRequest.getFirstName());

        when(userService.update(eq(userId), any(UserUpdateRequest.class)))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName")
                        .value("Updated First Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName")
                        .value("Last"));

        verify(userService, times(1)).update(eq(userId), any(UserUpdateRequest.class));
    }

    @Test
    public void testSearchUsers() throws Exception {
        int page = 0;
        int size = 10;
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("1986-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("1996-01-01");
        PageRequest pageable = PageRequest.of(page, size);

        List<User> userList = initUserList();

        Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());

        when(userService.findUsersByDateOfBirthBetween(fromDate, toDate, pageable)).thenReturn(userPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("fromDate", "1986-01-01")
                        .param("toDate", "1996-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].firstName").exists());
    }

    @Test
    public void testDeleteUser() throws Exception {
        Long userId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(userId);
    }

    private List<User> initUserList() throws Exception{
        List<User> userList = new ArrayList<>();

        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("First");
        user1.setLastName("Last");
        user1.setEmail("mail@mail.com");
        user1.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"));

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("First2");
        user2.setLastName("Last2");
        user2.setEmail("mail@mail.com");
        user2.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1987-01-01"));

        User user3 = new User();
        user3.setId(3L);
        user3.setFirstName("First3");
        user3.setLastName("Last3");
        user3.setEmail("mail@mail.com");
        user3.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1997-01-01"));

        User user4 = new User();
        user4.setId(4L);
        user4.setFirstName("First4");
        user4.setLastName("Last3");
        user4.setEmail("mail@mail.com");
        user4.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        return userList;
    }

}

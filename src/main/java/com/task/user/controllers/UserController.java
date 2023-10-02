package com.task.user.controllers;

import com.task.user.models.User;
import com.task.user.payloads.DataResponse;
import com.task.user.payloads.UserUpdateRequest;
import com.task.user.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse<User>> create(@Valid @RequestBody User user) {
        return ResponseEntity.status(201).body(new DataResponse<>(userService.save(user)));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse<User>> update(@Valid @RequestBody User user) {
        DataResponse<User> response = new DataResponse<>(userService.save(user));
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse<User>> update(@PathVariable Long id, @RequestBody UserUpdateRequest user) {
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(new DataResponse<>(updatedUser));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse<Page<User>>> searchUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate
    ) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users = userService.findUsersByDateOfBirthBetween(fromDate, toDate, pageable);
            DataResponse<Page<User>> response = new DataResponse<>(users);

            return ResponseEntity.ok(response);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }




}

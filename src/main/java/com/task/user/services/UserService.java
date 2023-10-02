package com.task.user.services;

import com.task.user.models.User;
import com.task.user.payloads.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface UserService {

    User findById(Long id);
    Page<User> findUsersByDateOfBirthBetween(Date startDate, Date endDate, Pageable pageable);

    User save(User user);

    User update(Long id, UserUpdateRequest user);

    void delete(Long id);
}

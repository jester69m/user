package com.task.user.repositories;

import com.task.user.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Page<User> findUsersByDateOfBirthBetween(Date startDate, Date endDate, Pageable pageable);
}

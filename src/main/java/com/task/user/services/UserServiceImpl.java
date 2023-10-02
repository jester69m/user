package com.task.user.services;

import com.task.user.exceptions.UserNotAcceptableAge;
import com.task.user.exceptions.UserNotFoundException;
import com.task.user.models.User;
import com.task.user.payloads.UserUpdateRequest;
import com.task.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    @Value("${user.acceptable.age}")
    private int acceptableAge;

    @Override
    public User findById(Long id) {
        log.info("Find user by id : {}", id);
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            return user.get();
        else
            throw new UserNotFoundException("User with id : " + id + " not found");
    }

    @Override
    public Page<User> findUsersByDateOfBirthBetween(Date startDate, Date endDate, Pageable pageable) {
        log.info("Find users by date of birth between : {} and {}", startDate, endDate);
        return userRepository.findUsersByDateOfBirthBetween(startDate, endDate, pageable);
    }

    @Override
    public User save(User user) {
        log.info("Save user : {}", user);
        if (isAgeAcceptable(user.getDateOfBirth(), acceptableAge)) {
            throw new UserNotAcceptableAge("User age must be greater than " + acceptableAge);
        }
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, UserUpdateRequest userUpdateRequest) {
        log.info("Update userUpdateRequest with id : {} and data : {}", id, userUpdateRequest);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id : " + id + " not found"));
        if(userUpdateRequest.getFirstName() != null)
            existingUser.setFirstName(userUpdateRequest.getFirstName());
        if(userUpdateRequest.getLastName() != null)
            existingUser.setLastName(userUpdateRequest.getLastName());
        if(userUpdateRequest.getEmail() != null)
            existingUser.setEmail(userUpdateRequest.getEmail());
        if(userUpdateRequest.getDateOfBirth() != null){
            if (isAgeAcceptable(userUpdateRequest.getDateOfBirth(), acceptableAge)) {
                throw new UserNotAcceptableAge("User age must be greater than " + acceptableAge);
            }
        else {existingUser.setDateOfBirth(userUpdateRequest.getDateOfBirth());}
        }
        if(userUpdateRequest.getAddress() != null)
            existingUser.setAddress(userUpdateRequest.getAddress());
        if(userUpdateRequest.getPhoneNumber() != null)
            existingUser.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        return userRepository.save(existingUser);
    }

    @Override
    public void delete(Long id) {
        log.info("Delete user with id : {}", id);
        userRepository.deleteById(id);
    }

    private boolean isAgeAcceptable(Date dateOfBirth, int acceptableAge) {
        LocalDate birthDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        int userAge = period.getYears();

        return userAge <= acceptableAge;
    }
}

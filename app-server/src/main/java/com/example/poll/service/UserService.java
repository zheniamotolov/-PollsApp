package com.example.poll.service;

import com.example.poll.exception.AppException;
import com.example.poll.model.User;
import com.example.poll.payload.UserSummaryUpdateDTO;
import com.example.poll.repository.UserRepository;
import com.example.poll.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    final private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User updateUserProfile(String username, UserSummaryUpdateDTO userSummaryUpdateDTO, UserPrincipal currentUser) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException("no such user"));

        if (userSummaryUpdateDTO.getName() != null) {
            user.setName(userSummaryUpdateDTO.getName());
        }

        return userRepository.save(user);


    }

}

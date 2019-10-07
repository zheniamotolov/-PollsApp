package com.example.poll.controller;

import com.example.poll.exception.ResourceNotFoundException;
import com.example.poll.model.User;
import com.example.poll.payload.*;
import com.example.poll.repository.PollRepository;
import com.example.poll.repository.UserRepository;
import com.example.poll.repository.VoteRepository;
import com.example.poll.security.CurrentUser;
import com.example.poll.security.UserPrincipal;
import com.example.poll.service.PollService;
import com.example.poll.service.UserService;
import com.example.poll.utill.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollService pollService;

    @Autowired
    private UserService userService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getName(), currentUser.getUsername());
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username")
                                                                      String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    @PreAuthorize("hasRole('USER')")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
//        try{
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount);
//    }
//        catch (AccessDeniedException e){
//            throw new ResponseStatusException(
//                    HttpStatus.UNAUTHORIZED, "You not authorised", e);
//        }
    }

    @GetMapping("users/{username}/polls")
    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsCreatedBy(username, currentUser, page, size);
    }

    @GetMapping("/users/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsVotedBy(username, currentUser, page, size);
    }

    @PatchMapping("/user/info/{username}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable(value = "username") String username,
            @Valid @RequestBody UserSummaryUpdateDTO userSummaryUpdateDTO,
            @CurrentUser UserPrincipal currentUser) {
        userService.updateUserProfile(username, userSummaryUpdateDTO, currentUser);
        return new ResponseEntity(new ApiResponse(true, "Updated"), HttpStatus.OK);
    }

}

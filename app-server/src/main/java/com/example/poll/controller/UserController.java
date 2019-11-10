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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    private final PollRepository pollRepository;

    private final VoteRepository voteRepository;

    private final PollService pollService;

    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, PollRepository pollRepository, VoteRepository voteRepository, PollService pollService, UserService userService) {
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
        this.pollService = pollService;
        this.userService = userService;
    }

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
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                         @RequestParam(value = "query", defaultValue = "") String query) {
        return pollService.getPollsCreatedBy(username, currentUser, page, size,query);
    }

    @GetMapping("/users/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                              @CurrentUser UserPrincipal currentUser,
                                              @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                              @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                       @RequestParam(value = "query", defaultValue = "") String query,
                                              @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
                                              @RequestParam(value = "direction", defaultValue = AppConstants.DEFAULT_SORT_ORDER) String direction
                                              ) {
        return pollService.getPollsVotedBy(username, currentUser,page,size,query,sortBy,direction);
    }

    @PatchMapping("/user/info/{username}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable(value = "username") String username,
            @Valid @RequestBody UserSummaryUpdateDTO userSummaryUpdateDTO,
            @CurrentUser UserPrincipal currentUser) {
        userService.updateUserProfile(username, userSummaryUpdateDTO, currentUser);
        return ResponseEntity.ok("Updated");
    }

}

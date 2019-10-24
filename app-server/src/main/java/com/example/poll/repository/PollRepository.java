package com.example.poll.repository;

import com.example.poll.model.buisness.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

public interface PollRepository extends CrudRepository<Poll, Long> {
    Optional<Poll> findById(Long pollId);

    Page<Poll> findByCreatedByAndQuestionContaining(Long userId,String question, Pageable pageable);

    Page<Poll> findByQuestionContaining(String question, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Poll> findByIdIn(List<Long> pollIds);


    List<Poll> findByIdInAndQuestionContaining(List<Long> pollIds,String question, Sort sort);
}

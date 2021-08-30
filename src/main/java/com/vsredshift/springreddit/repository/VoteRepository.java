package com.vsredshift.springreddit.repository;

import com.vsredshift.springreddit.model.Post;
import com.vsredshift.springreddit.model.User;
import com.vsredshift.springreddit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}

package com.vsredshift.springreddit.repository;

import com.vsredshift.springreddit.model.Post;
import com.vsredshift.springreddit.model.Subreddit;
import com.vsredshift.springreddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
}

package com.vsredshift.springreddit.repository;

import com.vsredshift.springreddit.model.Comment;
import com.vsredshift.springreddit.model.Post;
import com.vsredshift.springreddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);
}

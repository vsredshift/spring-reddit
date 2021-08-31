package com.vsredshift.springreddit.service;

import com.vsredshift.springreddit.dto.CommentsDto;
import com.vsredshift.springreddit.exception.PostNotFoundException;
import com.vsredshift.springreddit.mapper.CommentMapper;
import com.vsredshift.springreddit.model.Comment;
import com.vsredshift.springreddit.model.NotificationEmail;
import com.vsredshift.springreddit.model.Post;
import com.vsredshift.springreddit.model.User;
import com.vsredshift.springreddit.repository.CommentRepository;
import com.vsredshift.springreddit.repository.PostRepository;
import com.vsredshift.springreddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentsService {

    // TODO: Construct POST URL
    private static final String POST_URL = "";

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;

    public void createComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() +
                " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }


    public List<CommentsDto> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public List<CommentsDto> getCommentsByUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(
                user.getUsername() + " commented on your post", user.getEmail(), message));
    }

}

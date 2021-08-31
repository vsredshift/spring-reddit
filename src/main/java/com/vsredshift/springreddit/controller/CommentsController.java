package com.vsredshift.springreddit.controller;

import com.vsredshift.springreddit.dto.CommentsDto;
import com.vsredshift.springreddit.service.CommentsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {

    private CommentsService commentsService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        commentsService.createComment(commentsDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return status(OK).body(commentsService.getCommentsByPost(postId));
    }


    @GetMapping("by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsByUser(@PathVariable String userName) {
        return status(OK).body(commentsService.getCommentsByUser(userName));
    }
}

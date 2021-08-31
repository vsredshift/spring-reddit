package com.vsredshift.springreddit.service;

import com.vsredshift.springreddit.dto.VoteDto;
import com.vsredshift.springreddit.exception.PostNotFoundException;
import com.vsredshift.springreddit.exception.SpringRedditException;
import com.vsredshift.springreddit.model.Post;
import com.vsredshift.springreddit.model.Vote;
import com.vsredshift.springreddit.repository.PostRepository;
import com.vsredshift.springreddit.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.vsredshift.springreddit.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(
                        "Post with id " + voteDto.getPostId() + " not found"
                ));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(
                post, authService.getCurrentUser()
        );

        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType() +
                    "'d this post!");
        }

        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else post.setVoteCount(post.getVoteCount() - 1);

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}

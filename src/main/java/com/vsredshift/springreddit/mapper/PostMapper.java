package com.vsredshift.springreddit.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.vsredshift.springreddit.dto.PostRequest;
import com.vsredshift.springreddit.dto.PostResponse;
import com.vsredshift.springreddit.model.*;
import com.vsredshift.springreddit.repository.CommentRepository;
import com.vsredshift.springreddit.repository.VoteRepository;
import com.vsredshift.springreddit.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.vsredshift.springreddit.model.VoteType.DOWNVOTE;
import static com.vsredshift.springreddit.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mappings({
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
    @Mapping(target = "subreddit", source = "subreddit"),
    @Mapping(target = "user", source = "user"),
    @Mapping(target = "description", source = "postRequest.description"),
    @Mapping(target = "voteCount", constant = "0")})
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mappings({
    @Mapping(target = "id", source = "postId"),
    @Mapping(target = "postName", source = "postName"),
    @Mapping(target = "description", source = "description"),
    @Mapping(target = "url", source = "url"),
    @Mapping(target = "subredditName", source = "subreddit.name"),
    @Mapping(target = "userName", source = "user.username"),
    @Mapping(target = "commentCount", expression = "java(commentCount(post))"),
    @Mapping(target = "duration", expression = "java(getDuration(post))"),
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))"),
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")})
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(
                            post, authService.getCurrentUser()
                    );
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
        }
        return false;
    }
}

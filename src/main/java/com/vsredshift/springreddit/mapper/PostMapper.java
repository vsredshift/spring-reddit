package com.vsredshift.springreddit.mapper;

import com.vsredshift.springreddit.dto.PostRequest;
import com.vsredshift.springreddit.dto.PostResponse;
import com.vsredshift.springreddit.model.Post;
import com.vsredshift.springreddit.model.Subreddit;
import com.vsredshift.springreddit.model.User;
import com.vsredshift.springreddit.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mappings({
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
    @Mapping(target = "subreddit", source = "subreddit"),
    @Mapping(target = "user", source = "user"),
    @Mapping(target = "description", source = "postRequest.description")})
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mappings({
    @Mapping(target = "id", source = "postId"),
    @Mapping(target = "postName", source = "postName"),
    @Mapping(target = "description", source = "description"),
    @Mapping(target = "url", source = "url"),
    @Mapping(target = "subredditName", source = "subreddit.name"),
    @Mapping(target = "userName", source = "user.username")})
    public abstract PostResponse mapToDto(Post post);
}

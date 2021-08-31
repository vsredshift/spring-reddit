package com.vsredshift.springreddit.mapper;


import com.vsredshift.springreddit.dto.CommentsDto;
import com.vsredshift.springreddit.model.Comment;
import com.vsredshift.springreddit.model.Post;
import com.vsredshift.springreddit.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "text", source = "commentsDto.text"),
            @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
            @Mapping(target = "post", source = "post")
    })
    Comment map(CommentsDto commentsDto, Post post, User user);

    @Mappings({
            @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())"),
            @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    })
    CommentsDto mapToDto(Comment comment);
}

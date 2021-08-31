package com.vsredshift.springreddit.mapper;

import com.vsredshift.springreddit.dto.SubredditDto;
import com.vsredshift.springreddit.model.Post;
import com.vsredshift.springreddit.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "postCount", expression = "java(mapPosts(subreddit.getPosts()))")
    public abstract SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> postCount) {
        return postCount.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    public abstract Subreddit mapDtoToSubreddit(SubredditDto subreddit);
}

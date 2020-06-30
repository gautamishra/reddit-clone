package com.learning.redit.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.learning.redit.dto.SubReditDto;
import com.learning.redit.modal.Post;
import com.learning.redit.modal.Subreddit;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
	
   @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubReditDto mapSubredditToDto(Subreddit subreddit);
   

   default Integer mapPosts(List<Post> numberOfPosts) {
       return numberOfPosts.size();
   }
   
   @InheritInverseConfiguration
   @Mapping(target = "posts", ignore = true)
   Subreddit mapDtoToSubreddit(SubReditDto subreddit);
}

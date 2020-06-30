package com.learning.redit.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDto {
	 private Long id;
	    private Long postId;
	    private Instant createdDate;
	    private String text;
	    private String userName;
}

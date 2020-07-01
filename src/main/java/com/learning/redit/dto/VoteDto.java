package com.learning.redit.dto;

import com.learning.redit.modal.VoteType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {
	
	private VoteType voteType;
    private Long postId;
}

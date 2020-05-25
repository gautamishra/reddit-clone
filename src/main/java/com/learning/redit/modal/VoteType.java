package com.learning.redit.modal;

public enum VoteType {
	
	UPVOTE(1),
	DOWNVOTE(-1);
		
	int type;
	
	VoteType(int type){
		this.type = type;
	}
}

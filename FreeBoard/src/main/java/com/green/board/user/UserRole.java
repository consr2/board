package com.green.board.user;

import lombok.Getter;

@Getter
public enum UserRole {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER"),
	VISITER("ROLE_VISITER");
	
	UserRole(String value){
		this.value = value;
	}
	
	private String value;
}

package com.green.board.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

	@Size(min = 1, max = 25)
	@NotEmpty(message = "아이디 입력")
	private String username;
	
	@Size(min = 1, max = 16)
	@NotEmpty(message = "비번 입력")
	private String password1;
	
	@NotEmpty(message = "비번2 입력")
	private String password2;
	
	@NotEmpty(message = "메일 입력")
	@Email
	private String email;
	
}

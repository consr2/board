package com.green.board.user;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PwForm {
	
	@NotEmpty(message = "비번1 입력")
	private String password1;
	
	@NotEmpty(message = "비번2 입력")
	private String password2;
}

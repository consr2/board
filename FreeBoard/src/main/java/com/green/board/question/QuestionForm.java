package com.green.board.question;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {
	
	@NotEmpty(message = "제목없음")
	@Size(max = 200)
	private String subject;
	
	@NotEmpty(message = "내용없음")
	private String content;
	
}

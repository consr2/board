package com.green.board.answer;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {
	
	@NotEmpty(message = "빈칸 오류")
	private String content;
}

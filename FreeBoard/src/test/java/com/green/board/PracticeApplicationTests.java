package com.green.board;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.green.board.question.Question;
import com.green.board.question.QuestionRepository;
import com.green.board.question.QuestionService;

import lombok.RequiredArgsConstructor;


@SpringBootTest
class PracticeApplicationTests {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Test
	void jpatest() {
		for(int i = 0; i < 100; i++) {
			Question q = new Question();
			q.setContent("글내용이에요" + i);
			q.setSubject("제목 " + i);
			q.setCreateDate(LocalDateTime.now());
			questionRepository.save(q);
		}
	}

}

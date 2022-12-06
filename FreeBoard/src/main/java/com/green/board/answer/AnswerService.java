package com.green.board.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.green.board.alert.AlertsService;
import com.green.board.question.Question;
import com.green.board.user.SiteUser;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class AnswerService {

	private final AnswerRepository answerRepository;
	private final AlertsService alertsService;
	
	//댓글 생성
	public void create(Question question, String content, SiteUser author) {
		Answer a = new Answer();
		a.setContent(content);
		a.setQuestion(question);
		a.setAuthor(author);
		a.setCreateDate(LocalDateTime.now());
		answerRepository.save(a);
		alertsService.create(author, a.getQuestion().getAuthor(), a);
	}
	
	//댓글 pk로 찾기
	public Answer findById(Integer id) {
		Optional<Answer> oanswer = answerRepository.findById(id);
		return oanswer.get();
	}
	
	//댓글 삭제
	public void delete(Answer answer) {
		answerRepository.delete(answer);
	}
}

package com.green.board.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.green.board.categorie.Categorie;
import com.green.board.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	//게시글 만들기(제목,내용,유저,생성일자,카테고리,파일정보)
	public void create(String subject, String content, SiteUser author, Categorie categorie,Long fileId) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setAuthor(author);
		q.setCreateDate(LocalDateTime.now());
		q.setCategorie(categorie);
		q.setFileId(fileId);
		questionRepository.save(q);
	}
	
	//게시글 pk로 찾기
	public Question findById(Integer id) {
		Optional<Question> oquestion = questionRepository.findById(id);
		Question question = oquestion.get();
		return question;
	}
	
	//게시글 페이징 처리하기
	public Page<Question> getList(int page){
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
		return this.questionRepository.findAll(pageable);
	}
	
	//게시글 수정하기
	public void modify(Question q, String subject, String content, Long fileId) {
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setFileId(fileId);
		questionRepository.save(q);
	}
	
	//검색기능 + 페이징처리하기
	public Page<Question> searchSubject(String keyword, int page){
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
		return questionRepository.findBySubjectLike("%" + keyword + "%", pageable);
	}
	
	//게시글 방문자 수 추가
	public void view(Integer id) {
		Optional<Question> oquestion = questionRepository.findById(id);
		Question question = oquestion.get();
		int view = question.getView();
		question.setView(view + 1);
		questionRepository.save(question);
	}
	
	//게시글 추천 
	public void voter(Question question, SiteUser author) {
		Set<SiteUser> setAuthor = question.getVoter();
		setAuthor.add(author);
		question.setVoter(setAuthor);
		questionRepository.save(question);
	}
	
	//모든 게시글 찾기
	public List<Question> findAll() {
		List<Question> questionList = questionRepository.findAll();
		return questionList;
	}
	
	//모든 게시글 페이지 5단위로 찾기
	public Page<Question> findAllDate() {
		Pageable pageable = PageRequest.of(0, 5, Sort.by("createDate").descending());
		Page<Question> questionList = questionRepository.findAll(pageable);
		return questionList;
	}
	
	//카테고리별로 페이징 10개로 찾기
	public Page<Question> findByCategorie(Categorie categorie, int page){
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
		Page<Question> questionList = questionRepository.findByCategorie(categorie, pageable);
		return questionList;
	}
	
	//게시글 지우기
	public void delete(Question question) {
		questionRepository.delete(question);
	}
	
	
}

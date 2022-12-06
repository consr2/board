package com.green.board.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.green.board.answer.Answer;
import com.green.board.question.Question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, length = 20, nullable = false)
	private String username;
	
	private String password;
	
	@Column(unique = true)
	private String email;
	
	@Column
	private Integer role;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
	private List<Question> questionList;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;
}

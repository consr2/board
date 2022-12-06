package com.green.board.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.green.board.categorie.Categorie;

public interface QuestionRepository extends JpaRepository<Question, Integer>{
	Page<Question> findAll(Pageable pageable);
	Page<Question> findBySubjectLike(String keyword, Pageable pageable);
	Page<Question> findByCategorie(Categorie categorie, Pageable pageable);
}

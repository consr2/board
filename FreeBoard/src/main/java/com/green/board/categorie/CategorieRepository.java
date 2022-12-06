package com.green.board.categorie;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Integer>{
	Categorie findByname(String name);
}

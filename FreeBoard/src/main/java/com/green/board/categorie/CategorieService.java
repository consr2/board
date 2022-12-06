package com.green.board.categorie;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategorieService {

	private final CategorieRepository categorieRepository;
	
	//모든 카테고리
	public List<Categorie> findAll(){
		List<Categorie> categorieList = categorieRepository.findAll();
		return categorieList;
	}
	
	//카테고리 만들기
	public void createCate(String name) {
		Categorie c = new Categorie();
		c.setName(name);
		categorieRepository.save(c);
	}
	
	//카테고리 삭제
	public void delete(Integer id) {
		Optional<Categorie> c = categorieRepository.findById(id);
		categorieRepository.delete(c.get());
	}
	
	//카테고리 이름으로 찾기
	public Categorie findByname(String name) {
		Categorie categorie = categorieRepository.findByname(name);
		return categorie;
	}
	
	//카테고리 pk로 찾기
	public Categorie findById(Integer id) {
		Optional<Categorie> categorie = categorieRepository.findById(id);
		return categorie.get();
	}
	
}

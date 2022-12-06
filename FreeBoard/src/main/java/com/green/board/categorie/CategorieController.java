package com.green.board.categorie;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.green.board.alert.Alerts;
import com.green.board.alert.AlertsService;
import com.green.board.question.Question;
import com.green.board.question.QuestionService;
import com.green.board.user.SiteUser;
import com.green.board.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CategorieController {
	
	private final CategorieService categorieService;
	private final QuestionService questionService;
	private final UserService userService;
	private final AlertsService alertsService;
	
	//index페이지
	@RequestMapping("/")
	public String mainpage(Model model, @RequestParam(value = "page", defaultValue = "0")int page, Principal principal) {
		List<Categorie> categorieList = categorieService.findAll();
		Page<Question> qDateList = questionService.findAllDate();
		List<List<Question>> qList = mainPageList();
		
		if(principal != null) {
			SiteUser user = userService.getUser(principal.getName());
			List<Alerts> alertList = alertsService.findByUser(user);
			
			model.addAttribute("alertList", alertList);
		}
		
		model.addAttribute("qDateList", qDateList);
		model.addAttribute("qList", qList);
		model.addAttribute("categorieList", categorieList);

		return "mainpage";
	}
	
	//카테고리 추가화면
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/categorie/list")
	public String categorielist(Model model) {
		List<Categorie> categorieList = categorieService.findAll();
		model.addAttribute("categorieList", categorieList);
		return "categorieForm";
	}
	
	//카테고리 추가
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/categorie/create")
	public String categoriecreate(@RequestParam("name")String name) {
		categorieService.createCate(name);
		
		return "redirect:/categorie/list";
	}
	
	//카테고리 지우기
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping("/categorie/delete/{id}")
	public String categorieDelete(@PathVariable("id")Integer id) {
		categorieService.delete(id);	
		return "redirect:/categorie/list";
	}
	
	//모든 유저 관리창
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/user/list")
	public String userList(Model model) {
		List<SiteUser> userList = userService.getAllUser();
		model.addAttribute("userList", userList);
		return "userlistpage";
	}
	
	//유저 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping("/user/list/delete/{id}")
	public String deleteuser(Model model,@PathVariable("id")Long id) {
		SiteUser user = userService.findById(id);
		userService.delete(user);
		
		List<SiteUser> userList = userService.getAllUser();
		model.addAttribute("userList", userList);
		return "redirect:/user/list";
	}
	
	//유저 등업
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping("/user/list/up/{id}")
	public String upUser(Model model,@PathVariable("id")Long id) {
		SiteUser user = userService.findById(id);
		userService.upUser(user);
		List<SiteUser> userList = userService.getAllUser();
		model.addAttribute("userList", userList);
		return "redirect:/user/list";
	}
	
	// 유저 강등
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping("/user/list/down/{id}")
	public String downUser(Model model,@PathVariable("id")Long id) {
		SiteUser user = userService.findById(id);
		userService.downUser(user);
		List<SiteUser> userList = userService.getAllUser();
		model.addAttribute("userList", userList);
		return "redirect:/user/list";
	}
	
	//카테고리 별로 추리는 이중 배열	
	public List<List<Question>> mainPageList(){
			
		List<Categorie> categorieList = categorieService.findAll();
		List<List<Question>> questionList = new ArrayList<>();
		for(int i = 0; i < categorieList.size(); i++) {
			List<Question> qL = questionService.findAll();
			questionList.add(qL);
		}
		List<List<Question>> qList = new ArrayList<>();
		for(int i = 0; i < categorieList.size(); i++) {		
			List<Question> ql = new ArrayList<>();
			for(int k = 0; k < questionList.get(i).size(); k++) {
				Question q = questionList.get(i).get(k);
				if(q.getCategorie().getId().equals(categorieList.get(i).getId())){					
					ql.add(q);
				}
			}
			//생성날짜로 역순 정렬
			ql.sort(Comparator.comparing(Question::getCreateDate).reversed());
			
			qList.add(ql);
		}
		return qList;
	}
}

package com.green.board.user;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.green.board.alert.Alerts;
import com.green.board.alert.AlertsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

	private final UserService userService;
	private final AlertsService alertsService;
	
	//아이디 만드는 페이지 불러오기
	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}
	
	//아이디 만들기(아이디,비번,이메일 입력받음)
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm,BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "signup_form";
		}		
		if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호 불일치");
			return "signup_form";
		}
		
		try {
			userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());			
		}catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다");
			return "signup_form";
		}catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
		}
		
		return "redirect:/";
	}
	
	//로그인 페이지
	@GetMapping("/login")
	public String login() {
		return "login_form";
	}
	
	//유저 페이지
	@RequestMapping("/page/{id}")
	public String userPage(@PathVariable("id")String username, Model model) {
		SiteUser user = userService.getUser(username);
		
		model.addAttribute("user",user);
		return "userpage";
	}
	
	//비밀번호 변경 창 
	@RequestMapping("/newpassword")
	public String newpw(PwForm pwForm) {
		return "newpw";
	}
	
	//비밀번호 변경 완료
	@PostMapping("/newpassword/fin")
	public String pwChange(@Valid PwForm pwForm,BindingResult bindingResult, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "newpw";
		}		
		if(!pwForm.getPassword1().equals(pwForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호 불일치");
			return "newpw";
		}
				
		SiteUser user = userService.getUser(principal.getName());
		userService.newpw(user, pwForm.getPassword1());
		
		return "redirect:/";
	}
	
	//권한 부족 페이지 받기
	@RequestMapping("/auth/403")
	public  String error403() {
		
		return "/WEB/403auth";
	}

	
}

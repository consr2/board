package com.green.board.answer;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.green.board.alert.AlertsService;
import com.green.board.question.Question;
import com.green.board.question.QuestionService;
import com.green.board.user.SiteUser;
import com.green.board.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AnswerController {
	
	private final AnswerService answerService;
	private final QuestionService questionService;
	private final UserService userService;
	
	
	//댓글 작성
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/detail/create/{id}")
	public String createdetail(Model model, @PathVariable("id")Integer id,
			@Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
		Question question = this.questionService.findById(id);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		
		SiteUser siteuser = userService.getUser(principal.getName());
		answerService.create(question, answerForm.getContent(), siteuser);
		model.addAttribute("question", question);
		return String.format("redirect:/question/detail/%s", id);
	}
	
	//댓글 삭제
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("answer/delete/{id}")
	public String detailDelete(Principal principal, @PathVariable("id")Integer id) {
		Answer answer = answerService.findById(id);
		this.answerService.delete(answer);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}
	
}

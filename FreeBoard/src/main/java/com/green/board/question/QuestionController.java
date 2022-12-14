package com.green.board.question;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriUtils;

import com.google.gson.JsonObject;
import com.green.board.alert.Alerts;
import com.green.board.alert.AlertsService;
import com.green.board.answer.AnswerForm;
import com.green.board.categorie.Categorie;
import com.green.board.categorie.CategorieService;
import com.green.board.file.FileDto;
import com.green.board.file.FileService;
import com.green.board.user.SiteUser;
import com.green.board.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
	

	private final QuestionService questionService;
	private final UserService userService;
	private final CategorieService categorieService;
	private final FileService fileService;
	private final AlertsService alertsService;
	
	//?????? ??????????????? ??? ?????? ????????????
	@RequestMapping("/list/{cate}")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0")int page,
			@PathVariable("cate")Integer id, Principal principal) {
		Categorie categorie = categorieService.findById(id);
		List<Categorie> categorieList = categorieService.findAll();		
		Page<Question> paging = questionService.findByCategorie(categorie, page);
		
		//???????????? ??? ?????? ??????
		if(principal != null) {
			SiteUser user = userService.getUser(principal.getName());
			List<Alerts> alertList = alertsService.findByUser(user);
			model.addAttribute("alertList", alertList);
		}
		
		model.addAttribute("categorie", categorie);
		model.addAttribute("categorieList", categorieList);
		model.addAttribute("paging", paging);
		return "question_list";
	}
	
	//????????? ???????????? ??????
	@RequestMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm
			, Integer alertId, Principal principal) {
		Question question = questionService.findById(id);
		List<Categorie> categorieList = categorieService.findAll();
		
		//??????????????? ????????? ??????
		if(question.getFileId() != null) {
			FileDto file = fileService.findById(question.getFileId());
			model.addAttribute("file", file);
		}
		//?????? ?????? ?????????
		if(alertId != null) {
			alertsService.delete(alertId);
		}
		//?????? ????????????
		if(principal != null) {
			SiteUser user = userService.getUser(principal.getName());
			List<Alerts> alertList = alertsService.findByUser(user);
			
			model.addAttribute("alertList", alertList);
		}
		
		model.addAttribute("categorieList", categorieList);
		model.addAttribute("question", question);
		return "question_detail";
	}
	
	//????????? ????????? ???
	@RequestMapping("/view")
	@ResponseBody
	public String viewplus(@RequestParam("id")String id) {
		Integer sid = Integer.parseInt(id);
		questionService.view(sid);
		//log.info("aa");
		return id;
	}
	
	

	//??? ????????? ????????? ??????
	//@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping("/create/{id}")
	public String createDetail(@PathVariable("id")Integer id, QuestionForm questionForm, Model model) {
		List<Categorie> categorieList = categorieService.findAll();
		Categorie categorie = categorieService.findById(id);
		model.addAttribute("cate",categorie);
		model.addAttribute("categorieList", categorieList);
		return "question_form";
	}
	
	//??? ????????? ??????
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createFin(@PathVariable("id")Integer id, Model model, @Valid QuestionForm questionForm,BindingResult bindingResult,
			Principal principal, @RequestParam("categorie")String name
			, @RequestParam("file") MultipartFile files) {
		if(bindingResult.hasErrors()) {
			Categorie categorie = categorieService.findById(id);
			List<Categorie> categorieList = categorieService.findAll();
			model.addAttribute("cate",categorie);
			model.addAttribute("categorieList", categorieList);
			return "question_form";
		}
		//?????? ??????, ??? ??????, ???????????? ??????
		try {
			Long fileId = fileService.saveFile(files);
			Categorie categorie = categorieService.findByname(name);
			SiteUser siteuser = userService.getUser(principal.getName());
			questionService.create(questionForm.getSubject(), questionForm.getContent(), siteuser, categorie, fileId);
		}catch(Exception e){
			e.getStackTrace();
		}
		
		return "redirect:/";
	}
	
	//????????? ????????????
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String modifyDetail(QuestionForm questionForm, @PathVariable("id")Integer id,
			Principal principal, Model model) {
		Question question = this.questionService.findById(id);
		Categorie categorie = question.getCategorie();
			
		model.addAttribute("cate",categorie);
		questionForm.setContent(question.getContent());
		questionForm.setSubject(question.getSubject());
		return "question_form";
	}
	
	//????????? ?????? ??????
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
			@PathVariable("id")Integer id, Principal principal, @RequestParam("file") MultipartFile files, Model model) {
		if(bindingResult.hasErrors()) {
			Question question = this.questionService.findById(id);
			Categorie categorie = question.getCategorie();
			model.addAttribute("cate",categorie);
			return "question_Form";
		}
		Question question = this.questionService.findById(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new Error("????????? ?????????");
		}
		try {
			//????????? ?????? ??????????????? ?????? ????????? ?????? ??????
			if(question.getFileId() == null) {
				Long fileId = fileService.saveFile(files);
				this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent(), fileId);
			//????????? ????????? ?????? ?????? ??????
			}else {
				this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent(), question.getFileId());
			}
			
		}catch(Exception e){
			e.getStackTrace();
		}
		return String.format("redirect:/question/detail/%s", id);
	}
	
	//????????? ????????????
	@GetMapping("/search/{id}")
	public String questionSearch(@PathVariable("id")Integer id, @RequestParam(value = "keyword", defaultValue = "")String keyword,
			@RequestParam(value = "page", defaultValue = "0")int page, Model model) {
		
		Page<Question> paging = questionService.searchSubject(keyword, page);
		Categorie categorie = categorieService.findById(id);
		List<Categorie> categorieList = categorieService.findAll();
		
		model.addAttribute("categorieList", categorieList);
		model.addAttribute("categorie", categorie);
		model.addAttribute("paging", paging);
		model.addAttribute("keyword", keyword);
		return "question_list";
	}
	
	//????????? ??????
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/detail/{id}/voter")
	public String voter(@PathVariable("id")Integer id,Principal principal, Model model) {
		Question question = questionService.findById(id);
		SiteUser anthor = userService.getUser(principal.getName());
		if(question.getVoter().contains(anthor)) {
			boolean fail = false;
			model.addAttribute("fail", fail);
			return String.format("redirect:/question/detail/%s",id);
		}
		questionService.voter(question, anthor);
		return String.format("redirect:/question/detail/%s",id);
	}
	
	//????????? ??????
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/delete/{id}")
	public String deletequestion(@PathVariable("id")Integer id, Principal principal) {
		Question question = questionService.findById(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new Error("????????? ?????????");
		}
		
		questionService.delete(question);		
		return "redirect:/";
	}
	
	//???????????? ??????
	@RequestMapping("/download/{id}")
	public ResponseEntity<Resource> download(@PathVariable("id")Long id) throws IOException{
		//???????????? ?????? ?????? ????????????
		FileDto file = fileService.findById(id);
		//???????????? ?????? ?????? ????????????
		UrlResource resource = new UrlResource("file:" + file.getSavePath());
		//???????????? ?????? ?????????
		String encodedFileName = UriUtils.encode(file.getOrginName(), StandardCharsets.UTF_8);
		//?????? ?????? ??? ?????? ?????? ??????
		String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition).body(resource);
	}
	
	//????????? ??????
	@GetMapping("/images/{fileId}")
	@ResponseBody
	public Resource showImage(@PathVariable("fileId") Long id, Model model) throws IOException{
		FileDto file = fileService.findById(id);
		
		return new UrlResource("file:" + file.getSavePath());
	}

	@PostMapping("/summernote")
	@ResponseBody
	public String uploadSummernoteImageFile(MultipartHttpServletRequest request) {
		
		Map<String, MultipartFile> mf = request.getFileMap();
		MultipartFile file = mf.get("file");
		
		try {
			Long fileId = fileService.saveFile(file);
			String Path = "/question/images/" + fileId; 
			return Path;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return "?????? ??????";
	}

	
	
	
}
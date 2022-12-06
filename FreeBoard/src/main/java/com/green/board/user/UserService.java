package com.green.board.user;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	//이름,메일,비번으로 아이디 생성
	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(1);
		this.userRepository.save(user);
		return user;
	}
	
	//유저 이름으로 유저 정보찾기
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = userRepository.findByusername(username);
		return siteUser.get();
	}
	
	//모든 유저 정보 찾기
	public List<SiteUser> getAllUser(){
		List<SiteUser> userList = userRepository.findAll();
		return userList;
	}
	
	//pk로 유저 정보 찾기
	public SiteUser findById(Long id) {
		Optional<SiteUser> oUser = userRepository.findById(id);
		return oUser.get();
	}
	//유저 삭제
	public void delete(SiteUser user) {	
		userRepository.delete(user);
	}
	
	//유저 권한 상승
	public void upUser(SiteUser user) {
		Integer i = user.getRole();
		user.setRole(i+1);
		userRepository.save(user);
	}
	
	//유저 권한 하락
	public void downUser(SiteUser user) {
		Integer i = user.getRole();
		user.setRole(i-1);
		userRepository.save(user);
	}
	
	//비밀번호 변경
	public void newpw(SiteUser user, String pw) {
		user.setPassword(passwordEncoder.encode(pw));
		this.userRepository.save(user);
		
	}
	
	
}

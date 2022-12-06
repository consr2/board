package com.green.board.alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.green.board.answer.Answer;
import com.green.board.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AlertsService {

	private final AlertsRepository alertRepository;
	
	//알람 추가
	public void create(SiteUser user, SiteUser host, Answer answer) {
		Alerts alert = new Alerts();
		alert.setUser(user);
		alert.setHost(host);
		alert.setAnswer(answer);
		alertRepository.save(alert);
	}
	
	//접속중인 아이디의 알림가져오기 + 본인의 댓글 알림은 뺴기
	public List<Alerts> findByUser(SiteUser host){
		List<Alerts> alertList = alertRepository.findByHost(host);
		List<Alerts> newAlertList = new ArrayList<Alerts>();
		for(Alerts a : alertList) {
			if(!a.getUser().getId().equals(host.getId())) {
				newAlertList.add(a);
			}
		}
		
		return newAlertList;
	}
	
	//알림 지우기
	public void delete(Integer id) {
		Optional<Alerts> alert =  alertRepository.findById(id);
		alertRepository.delete(alert.get());
	}
	
}

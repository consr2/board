package com.green.board.alert;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.green.board.user.SiteUser;

public interface AlertsRepository extends JpaRepository<Alerts, Integer> {
	public List<Alerts> findByHost(SiteUser host);
}

package com.green.board.file;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.green.board.question.Question;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class FileDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String orginName;
	
	private String saveName;
	
	private String savePath;
	
	
	@Builder
	public FileDto(Long id, String orginName, String saveName, String savePath) {
		this.id = id;
		this.orginName = orginName;
		this.saveName = saveName;
		this.savePath = savePath;
	}
}

	

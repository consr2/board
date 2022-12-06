package com.green.board.file;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

	@Value("${fileDir}")
	private String fileDir;
	
	private final FileRepository fileRepository;
	
	public Long saveFile(MultipartFile files) throws IOException {
		if(files.isEmpty()) {
			return null;
		}
		log.info("파일 생성 : " + fileDir);
		//파일 이름 추출
		String origName = files.getOriginalFilename();
		//파일 랜덤uuid생성
		String uuid = UUID.randomUUID().toString();
		//확장자 추출
		String extension = origName.substring(origName.lastIndexOf("."));
		//저장 이름
		String saveName = uuid + extension;
		//저장 경로
		String savePath = fileDir + "\\" +saveName;
		
		
		FileDto file = FileDto.builder()
				.orginName(origName)
				.saveName(saveName)
				.savePath(savePath)
				.build();
		
		//mkdir = 파일 저장 할 폴더 생성
		File fileFolder = new File(fileDir);
		fileFolder.mkdir();
		
		
		//지정한 경로에 파일 저장
		files.transferTo(new File(savePath));
		//DB에도 파일 저장
		fileRepository.save(file);
		
		return file.getId();
	}
	
	public FileDto findById(Long id) {	
		return fileRepository.findById(id).get();
	}

}

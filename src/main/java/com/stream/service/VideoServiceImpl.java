package com.stream.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.stream.entity.Video;
import com.stream.repository.VideoRepository;

import jakarta.annotation.PostConstruct;


@Service
public class VideoServiceImpl implements VideoService{
	@Autowired
	private VideoRepository videoRepo;
	
	@Value("{files.video}")
	String DIR;
	
	@PostConstruct
	public void init() {
		
		File file= new File(DIR);
		if(!file.exists()) {
			file.mkdir();
			System.out.println("Folder Created");
		}else {
			System.out.println("Folder Already Created");
		}
		
	}

	@Override
	public Video save(Video video, MultipartFile file) {
		//original file name
		try {
			
		
		String filename=file.getOriginalFilename();
		String contentType=file.getContentType();
		InputStream inputStream = file.getInputStream();
		
		
		//file path
		String cleanFfileName = StringUtils.cleanPath(filename);
		
		//folder path: create
		String cleanFolder = StringUtils.cleanPath(DIR);
		
		//folder path with filename
		Path path = Paths.get(cleanFolder, cleanFfileName);
		System.out.println(contentType);
		System.out.println(path);
		
	
		
		//copy file to the folder
		Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		
		//video meta data
		video.setContentType(contentType);
		video.setFilePath(path.toString());
		
		//metadata save
		
		return videoRepo.save(video);
		
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Video get(String videoId) {
		Video video = videoRepo.findById(videoId).orElseThrow(()->new RuntimeException("video not found"));

		return video;
	}

	@Override
	public Video getByTitle(String title) {

		return null;
	}

	@Override
	public List<Video> getAll() {

		return videoRepo.findAll();
	}

}

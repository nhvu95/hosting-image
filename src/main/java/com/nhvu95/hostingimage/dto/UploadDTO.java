package com.nhvu95.hostingimage.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadDTO {
	private MultipartFile uploadFile;
	private UploadConfig config;
}

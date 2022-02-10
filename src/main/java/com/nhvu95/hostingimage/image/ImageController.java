package com.nhvu95.hostingimage.image;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nhvu95.hostingimage.dto.UploadConfig;
import com.nhvu95.hostingimage.dto.UploadDTO;
import com.nhvu95.hostingimage.imgur.Variation;

@RestController
@RequestMapping(value = "/image", produces = "application/json")
public class ImageController {

	@Autowired
	ImageService service;

	@PostMapping
	public Variation uploadImage(@RequestPart MultipartFile file, @RequestPart UploadConfig config) {
		try {
			return service.uploadImage(file.getInputStream(), file.getOriginalFilename(), config);
		} catch (IOException e) {
			// Error upload file
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Image> getListImage() {
		return service.getImageList();
	}
}

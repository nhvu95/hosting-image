package com.nhvu95.hostingimage.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.util.IOUtils;
import com.nhvu95.hostingimage.dto.UploadConfig;
import com.nhvu95.hostingimage.imgur.Variation;

@RestController
@RequestMapping(value = "/image", produces = "application/json")
public class ImageController {

	ImageService service;

	@Autowired
	public ImageController(ImageService service) {
		this.service = service;
	}

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

	@GetMapping(value = "/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity getImage(@PathVariable String fileName, HttpServletResponse response) {
		File resource = new File(service.getImage(fileName));
		if (!resource.exists())
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		try {
			OutputStream outStream = response.getOutputStream();
			IOUtils.copy(new FileInputStream(resource), outStream, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setCacheControl(CacheControl.maxAge(365L, TimeUnit.DAYS).mustRevalidate().noTransform());
		return ResponseEntity.ok().headers(responseHeaders).build();
	}
}

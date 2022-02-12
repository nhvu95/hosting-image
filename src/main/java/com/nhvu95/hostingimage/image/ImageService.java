package com.nhvu95.hostingimage.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nhvu95.hostingimage.dto.UploadConfig;
import com.nhvu95.hostingimage.imgur.Variation;
import com.nhvu95.hostingimage.imgur.VariationService;

@Service
public interface ImageService {
	public Variation uploadImage(InputStream image, String fileName, UploadConfig config) throws IOException;

	public List<Image> getImageList();

	public String getImage(String fileId);
}

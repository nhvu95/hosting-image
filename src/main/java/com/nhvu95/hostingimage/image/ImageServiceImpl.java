package com.nhvu95.hostingimage.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nhvu95.hostingimage.ds.ConfigDS;
import com.nhvu95.hostingimage.dto.UploadConfig;
import com.nhvu95.hostingimage.imgur.VariationService;
import com.nhvu95.hostingimage.imgur.Variation;

@Service
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepo;
	private final ImageComputeService imageCompute;
	private final VariationService imgurService;

	@Value("${image.temp.path}")
	private String tempPath = "./images";

	@Autowired
	ImageServiceImpl(ImageRepository imageRepo, ImageComputeService imageCompute, VariationService imgurService) {
		this.imageRepo = imageRepo;
		this.imageCompute = imageCompute;
		this.imgurService = imgurService;
	}

	public Variation uploadImage(InputStream image, String fileName, UploadConfig cofigDTO) throws IOException {
		Image original = this.imageRepo.save(new Image());
		ConfigDS configDS = new ConfigDS(cofigDTO, fileName);
		// Save Original Image Fist
		StringBuilder originBuilder = new StringBuilder().append(String.format("%s/%s/%dx%d.%s", tempPath,
				original.getId(), configDS.getWidth(), configDS.getHeight(), configDS.getType().getStringValue()));
		new File(originBuilder.toString()).mkdirs();
		
		Files.copy(image, Paths.get(originBuilder.toString()), StandardCopyOption.REPLACE_EXISTING);
		Variation originVariation = imgurService.uploadToImgur(new File(originBuilder.toString()), true);

		// Calculate Variation
		String filePath = imageCompute.calculateVariation(new FileInputStream(originBuilder.toString()), original.id.toString(), configDS);
		if (filePath == null)
			throw new IOException("Calculate failed");

		if (configDS.isImgurMode()) {
			Variation imgurOut = imgurService.uploadToImgur(new File(filePath), false);
			return imgurOut;
		} else {
			return this.hostImage(filePath);
		}
	}

	public Variation hostImage(String filePath) {
		return new Variation(filePath, 0, 0, true);
	}

	public List<Image> getImageList() {
		List<Image> result = this.imageRepo.findAll();
		return result;
	}

}

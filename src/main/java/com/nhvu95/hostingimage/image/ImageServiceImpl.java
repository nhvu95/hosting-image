package com.nhvu95.hostingimage.image;

import java.awt.Rectangle;
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

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nhvu95.hostingimage.ds.ConfigDS;
import com.nhvu95.hostingimage.dto.UploadConfig;
import com.nhvu95.hostingimage.imgur.VariationService;
import com.nhvu95.hostingimage.utilities.Utilities;
import com.nhvu95.hostingimage.imgur.Variation;
import com.nhvu95.hostingimage.imgur.VariationRepository;

@Service
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepo;
	private final ImageComputeService imageCompute;
	private final VariationService imgurService;
	private final VariationRepository variationRepo;

	@Value("${image.temp.path}")
	final private String TEMP_PATH = "./images";

	@Autowired
	ImageServiceImpl(ImageRepository imageRepo, ImageComputeService imageCompute, VariationService imgurService,
			VariationRepository variationRepo) {
		this.imageRepo = imageRepo;
		this.imageCompute = imageCompute;
		this.imgurService = imgurService;
		this.variationRepo = variationRepo;
	}

	@Transactional
	public Variation uploadImage(InputStream image, String fileName, UploadConfig cofigDTO) throws IOException {
		Image original = new Image();
		this.imageRepo.save(original);

		Variation imgurOut;
		ConfigDS configDS = new ConfigDS(cofigDTO, fileName);
		String fileType = configDS.getType().getStringValue();
		// Save origin file name from stream upload
		String originalFilePath = String.format("%s/%s/%s.%s", this.TEMP_PATH, original.getId(), "origin", fileType);
		File originalFile = new File(originalFilePath);
		originalFile.mkdirs(); // Create folder if not exist.
		Files.copy(image, Paths.get(originalFilePath), StandardCopyOption.REPLACE_EXISTING);
		Rectangle originalSize = Utilities.getImageSize(originalFile);

		// If size is same with original
		if (configDS.isAutoSize() || originalSize.equals(configDS.getImageSize())) {
			// Compress Original Image
			this.imageCompute.compressImage(originalFilePath);
			if (configDS.isImgurMode()) {
				imgurOut = imgurService.uploadToImgur(originalFile, true);
				Utilities.delelteFile(originalFile);
			} else
				imgurOut = this.hostImage(String.format("%s/%s.%s", this.TEMP_PATH, original.getId(), fileType),
						(int) originalSize.getWidth(), (int) originalSize.getHeight(), true);
		} else {
			// Calculate Variation
			String variationFilePath = imageCompute.calculateVariation(new FileInputStream(originalFilePath),
					original.getId().toString(), configDS);
			// Compress Variation Image
			this.imageCompute.compressImage(variationFilePath);

			if (variationFilePath == null)
				throw new IOException("Calculate failed");

			if (configDS.isImgurMode()) {
				File variation = new File(variationFilePath);
				imgurOut = imgurService.uploadToImgur(variation, false);
				Utilities.delelteFile(variation);
			} else {
				imgurOut = this.hostImage(variationFilePath, configDS.getWidth(), configDS.getHeight(), false);
			}
		}
		original.addVariation(imgurOut);
		original.handled = true;
		return imgurOut;
	}

	public Variation hostImage(String filePath, int width, int height, boolean original) {
		filePath = filePath.substring(this.TEMP_PATH.length());
		Variation hostImage = new Variation(filePath, width, height, original);
		return hostImage;
	}

	public List<Image> getImageList() {
		List<Image> result = this.imageRepo.findAll();
		return result;
	}

	@Override
	public String getImage(String fileName) {
		// TODO Auto-generated method stub
		return String.format("%s/%s/origin.%s", this.TEMP_PATH, Utilities.getFileNameWithoutType(fileName),
				Utilities.getFileExtension(fileName));
	}

}

package com.nhvu95.hostingimage.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nhvu95.hostingimage.ds.ConfigDS;
import com.nhvu95.hostingimage.imgur.Variation;
import com.nhvu95.hostingimage.imgur.VariationRepository;
import com.nhvu95.hostingimage.utilities.Utilities;

import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.Compressor;
import lombok.extern.log4j.Log4j;

@Service
public class ImageComputeServiceImpl implements ImageComputeService {
	@Value("${image.temp.path}")
	private String tempPath = "./images";

	private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(ImageComputeService.class);

	public String calculateVariation(InputStream image, String originUUID, ConfigDS config) {

		String stockExtension = config.getType().getStringValue();
		try {
			Image originalImage = ImageIO.read(image);
			ImageIcon imgIcon = new ImageIcon(originalImage);
			int lastW = imgIcon.getIconWidth();
			int lastH = imgIcon.getIconHeight();

			int newW = lastW, newH = lastH;
			float ratio = 1;

			if (config.isKeepRatio()) {
				ratio = lastW / lastH;
				newH = (int) Math.ceil(config.getWidth() / ratio);
			}

			Image newImg = scaleImage(originalImage, newW, newH);
			// Save Variation Image
			StringBuilder pathBuilder = new StringBuilder()
					.append(String.format("%s/%s/%dx%d.%s", tempPath, originUUID, newW, newH, stockExtension));
			String outputFilePath = pathBuilder.toString();
			savePic(newImg, stockExtension, outputFilePath, newW, newH);
			compressImage(outputFilePath);
			return outputFilePath;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Image scaleImage(Image image, int width, int height) {
		return image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	}

	public void compressImage(String filePath) throws IOException {

		try {
			Compressor compressor = new Compressor(CompressionMode.GZ);
			compressor.compress(filePath, tempPath + "/" + "compressed." + Utilities.getFileExtension(filePath), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void savePic(Image image, String type, String dst, int w, int h) {
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = bi.getGraphics();
		try {
			g.drawImage(image, 0, 0, null);
			ImageIO.write(bi, type, new File(dst));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

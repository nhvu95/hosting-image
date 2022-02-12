package com.nhvu95.hostingimage.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nhvu95.hostingimage.ds.ConfigDS;
import com.nhvu95.hostingimage.utilities.Utilities;
import com.tinify.AccountException;
import com.tinify.ClientException;
import com.tinify.ConnectionException;
import com.tinify.ServerException;
import com.tinify.Tinify;

@Service
public class ImageComputeServiceImpl implements ImageComputeService {
	@Value("${image.temp.path}")
	private String tempPath = "./images";
	@Value("${tinify.api.key}")
	private String TINIFY_API_KEY = "";

	private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(ImageComputeService.class);

	public ImageComputeServiceImpl() {
		Tinify.setKey(this.TINIFY_API_KEY);
	}

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
			File tmp = new File(filePath);
			tmp.getParent();
			Tinify.fromFile(filePath).toFile(filePath);

			// Use the Tinify API client.
		} catch (AccountException e) {
			System.out.println("The error message is: " + e.getMessage());
			// Verify your API key and account limit.
		} catch (ClientException e) {
			// Check your source image and request options.
			this.log.error(e.toString());
		} catch (ServerException e) {
			// Temporary issue with the Tinify API.
			this.log.error(e.toString());
		} catch (ConnectionException e) {
			// A network connection error occurred.
			this.log.error(e.toString());
		} catch (java.lang.Exception e) {
			// Something else went wrong, unrelated to the Tinify API.
			this.log.error(e.toString());
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

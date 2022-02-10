package com.nhvu95.hostingimage.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nhvu95.hostingimage.ds.ConfigDS;
import com.nhvu95.hostingimage.imgur.Variation;
import com.nhvu95.hostingimage.imgur.VariationRepository;
import com.nhvu95.hostingimage.utilities.Utilities;

@Service
public class ImageComputeServiceImpl implements ImageComputeService {
	@Value("${image.temp.path}")
	private String tempPath = "./images";

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
			savePic(newImg, stockExtension, pathBuilder.toString(), newW, newH);

			return pathBuilder.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Image scaleImage(Image image, int width, int height) {
		return image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
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

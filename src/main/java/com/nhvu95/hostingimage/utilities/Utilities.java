package com.nhvu95.hostingimage.utilities;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.nhvu95.hostingimage.ds.EImageType;

public class Utilities {
	public static String getFileNameWithoutType(String fileName) {
		return fileName.replaceFirst("[.][^.]+$", "");
	}

	public static String getFileExtension(String fileName) {
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i + 1);
		}
		return extension;
	}

	public static EImageType getFileExtensionE(String fileName) {
		String extension = getFileExtension(fileName);
		switch (extension.toLowerCase()) {
		case "png":
			return EImageType.PNG;
		case "jpg":
		case "jpeg":
			return EImageType.JPG;
		case "svg":
			return EImageType.SVG;
		}
		return EImageType.PNG;
	}

	public static Rectangle getImageSize(File file) {
		Image originalImage;
		try {
			originalImage = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		ImageIcon imgIcon = new ImageIcon(originalImage);
		int width = imgIcon.getIconWidth();
		int height = imgIcon.getIconHeight();
		return new Rectangle(width, height);
	}

	public static void delelteFile(File file) {
		try {
			file.deleteOnExit();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}

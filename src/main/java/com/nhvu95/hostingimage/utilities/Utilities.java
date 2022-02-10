package com.nhvu95.hostingimage.utilities;

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
}

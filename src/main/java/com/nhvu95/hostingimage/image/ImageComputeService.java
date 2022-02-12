package com.nhvu95.hostingimage.image;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.nhvu95.hostingimage.ds.ConfigDS;

@Service
public interface ImageComputeService {

	public String calculateVariation(InputStream image, String uuid, ConfigDS config);

	public Image scaleImage(Image image, int width, int height);
	
	public void compressImage(String imagePath) throws IOException;

	public void savePic(Image image, String type, String dst, int w, int h);
}

package com.nhvu95.hostingimage.imgur;

import com.github.kskelm.baringo.BaringoClient;
import com.github.kskelm.baringo.model.Image;
import com.github.kskelm.baringo.util.BaringoApiException;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VariationService {
	String clientId = "f1d4701088cfb34"; // from registration
	String clientSecret = "37248f9b7c54941c061bdb713ef96a5af0dbe98d"; // from registration
	BaringoClient client;

	@Autowired
	VariationService() {
		try {
			// Set up an authenticated APIClient with your clientKey and
			// clientSecret as issued by Imgur.
			client = new BaringoClient.Builder().clientAuth(clientId, clientSecret).build();
		} catch (BaringoApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Variation uploadToImgur(File image, boolean origin) {
		// Get the info about an image
		String path = image.getAbsolutePath();
		String fileName = image.getName();
		try {
			Image resImgur = client.imageService().uploadLocalImage(null, // infer mime type from filename
					path, null, fileName, "ImageHosting");
			Variation out = new Variation(resImgur.getLink(), resImgur.getWidth(), resImgur.getHeight(), origin);
			System.out.println(out);
			return out;
		} catch (BaringoApiException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

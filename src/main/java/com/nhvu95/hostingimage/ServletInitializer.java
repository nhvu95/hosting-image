package com.nhvu95.hostingimage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
	@Value("${image.temp.path}")
	private String tempPath = "./images";

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		Path saveDisk = Paths.get(tempPath);
		if (!Files.exists(saveDisk)) {
			try {
				Files.createDirectories(saveDisk, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return application.sources(HostingImageApplication.class);
	}

}

package com.nhvu95.hostingimage.ds;

import java.awt.Rectangle;

import org.springframework.beans.factory.annotation.Value;

import com.nhvu95.hostingimage.dto.UploadConfig;
import com.nhvu95.hostingimage.utilities.Utilities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigDS {
	int width = 0;
	int height = 0;
	EImageType type = EImageType.PNG;
	boolean keepRatio = true;
	boolean lazyLoad = true;
	boolean imgurMode = true;

	public ConfigDS(UploadConfig config, String fileName) {
		this.type = Utilities.getFileExtensionE(fileName);
		this.width = config.width;
		this.height = config.height;
		this.keepRatio = config.keepRatio;
		this.lazyLoad = config.lazyLoad;
		this.imgurMode = config.imgurMode;
	}

	public Rectangle getImageSize() {
		return new Rectangle(this.width, this.height);
	}

	public boolean isAutoSize() {
		return this.width == 0 && this.height == 0;
	}
}

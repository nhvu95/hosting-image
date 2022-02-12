package com.nhvu95.hostingimage.imgur;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nhvu95.hostingimage.image.Image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "variation")
public class Variation {

	public Variation(String link, int width, int height, boolean original) {
		super();
		this.link = link;
		this.width = width;
		this.height = height;
		this.original = original;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	int id;

	@Column(name = "link")
	String link;

	@Column(name = "width")
	int width;

	@Column(name = "height")
	int height;

	@Column(name = "modified_date", columnDefinition = "TIMESTAMP DEFAULT now()", nullable = false, insertable = false)
	Timestamp modifiedDate;

	@Column(name = "original")
	boolean original;

	@ManyToOne()
	@JsonIgnore()
	@JoinColumn(name = "image_id", nullable = false)
	Image image;

}

package com.nhvu95.hostingimage.image;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Image() {
		super();
		this.handled = false;
	}

	@Id
	@GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
	@GeneratedValue(generator = "UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	UUID id;

	@Column(name = "upload_date", columnDefinition = "TIMESTAMP DEFAULT now()", nullable = false, insertable = false)
	Timestamp uploadDate;

	@Column(name = "handled")
	boolean handled;
}

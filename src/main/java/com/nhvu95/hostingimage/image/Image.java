package com.nhvu95.hostingimage.image;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import com.nhvu95.hostingimage.imgur.Variation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "image")
public class Image implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Image() {
		super();
		this.handled = false;
		this.variations = new HashSet<Variation>();
	}

	@Id
    @GenericGenerator(name = "id", strategy = "com.nhvu95.hostingimage.utilities.ImageIdGenerator")
	@GeneratedValue(generator = "id")
	@Column(name = "id", updatable = false, nullable = false)
	String id;

	@Column(name = "upload_date", insertable = false, updatable = false)
	Timestamp uploadDate;

	@Column(name = "owner")
	UUID owner;
	
	@Column(name = "handled")
	boolean handled;

	@OneToMany(mappedBy = "image", cascade = { CascadeType.ALL })
	Set<Variation> variations;

	public void addVariation(Variation variation) {
		variations.add(variation);
		variation.setImage(this);
	}
}

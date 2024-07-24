package com.urlshortner.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "_urlinfo")
@RequiredArgsConstructor
public class UrlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "full_url")
	private String fullUrl;

	@Column(name = "short_url")
	private String shortUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public UrlEntity(Long id, String fullUrl, String shortUrl) {
		super();
		this.id = id;
		this.fullUrl = fullUrl;
		this.shortUrl = shortUrl;
	}

	public UrlEntity(String fullUrl) {
		super();
		this.fullUrl = fullUrl;
	}
	
	

}

package com.urlshortner.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.urlshortner.common.ShorteningUtil;
import com.urlshortner.dto.FullUrl;
import com.urlshortner.dto.ShortUrl;
import com.urlshortner.model.UrlEntity;
import com.urlshortner.repository.UrlRepository;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {
	@InjectMocks
	private UrlService urlService;

	@Mock
	private UrlRepository urlRepository;

	private FullUrl fullUrl;
	private UrlEntity urlEntity;

	@BeforeEach
	void setUp() {
		fullUrl = new FullUrl("http://example.com");
		urlEntity = new UrlEntity("http://example.com");
		urlEntity.setId(1L);
	}

	@Test
	void getShortUrlNewUrlTest() {
		when(urlRepository.findUrlByFullUrl(anyString())).thenReturn(Collections.emptyList());
		when(urlRepository.save(any(UrlEntity.class))).thenReturn(urlEntity);
		String expectedShortUrlText = ShorteningUtil.idToStr(urlEntity.getId());

		ShortUrl shortUrl = urlService.getShortUrl(fullUrl);

		assertEquals(expectedShortUrlText, shortUrl.getShortUrl());
		verify(urlRepository, times(1)).findUrlByFullUrl(anyString());
		verify(urlRepository, times(1)).save(any(UrlEntity.class));
	}

	@Test
	void getShortUrlExistingUrlTest() {
		when(urlRepository.findUrlByFullUrl(anyString())).thenReturn(List.of(urlEntity));
		String expectedShortUrlText = ShorteningUtil.idToStr(urlEntity.getId());

		ShortUrl shortUrl = urlService.getShortUrl(fullUrl);

		assertEquals(expectedShortUrlText, shortUrl.getShortUrl());
		verify(urlRepository, times(1)).findUrlByFullUrl(anyString());
		verify(urlRepository, never()).save(any(UrlEntity.class));
	}



}
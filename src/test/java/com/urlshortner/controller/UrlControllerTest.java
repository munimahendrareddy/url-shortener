package com.urlshortner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import com.urlshortner.common.UrlUtil;
import com.urlshortner.dto.FullUrl;
import com.urlshortner.dto.ShortUrl;
import com.urlshortner.error.InvalidUrlError;
import com.urlshortner.service.UrlService;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)

public class UrlControllerTest {

	@InjectMocks
	private UrlController urlController;

	@Mock
	private UrlService urlService;
	
	@Mock
	private MockMvc mockMvc;

	@Test
	void saveUrlTest() throws Exception {

		FullUrl fullUrl = new FullUrl();
		fullUrl.setFullUrl("http://example.com");

		HttpServletRequest request = new MockHttpServletRequest();
		((MockHttpServletRequest) request).setServerName("localhost");
		((MockHttpServletRequest) request).setServerPort(8080);
		((MockHttpServletRequest) request).setScheme("http");

		ShortUrl shortUrl = new ShortUrl();
		shortUrl.setShortUrl("/shortUrl");

		when(urlService.getShortUrl(fullUrl)).thenReturn(shortUrl);

		ResponseEntity<Object> responseEntity = urlController.saveUrl(fullUrl, request);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());

	}

	@Test
	void SaveUrlWithInvalidUrlTest() {
		FullUrl fullUrl = new FullUrl();
		fullUrl.setFullUrl("invalid_url");

		HttpServletRequest request = new MockHttpServletRequest();

		ResponseEntity<Object> responseEntity = urlController.saveUrl(fullUrl, request);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		InvalidUrlError error = (InvalidUrlError) responseEntity.getBody();
		assertEquals("Invalid URL", error.getMessage());
	}

	@Test
	void SaveUrlResponseStatusExceptionTest() throws Exception {
		FullUrl fullUrl = new FullUrl();
		fullUrl.setFullUrl("http://example.com");

		HttpServletRequest request = new MockHttpServletRequest();
		((MockHttpServletRequest) request).setServerName("localhost");
		((MockHttpServletRequest) request).setServerPort(8080);
		((MockHttpServletRequest) request).setScheme("http");

		try (MockedStatic<UrlUtil> mockedStatic = mockStatic(UrlUtil.class)) {
			mockedStatic.when(() -> UrlUtil.getBaseUrl(anyString())).thenThrow(new MalformedURLException());

			assertThrows(ResponseStatusException.class, () -> {
				urlController.saveUrl(fullUrl, request);
			});
		}
	}
	
	
}

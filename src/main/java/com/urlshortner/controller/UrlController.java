package com.urlshortner.controller;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.urlshortner.common.UrlUtil;
import com.urlshortner.dto.FullUrl;
import com.urlshortner.dto.ShortUrl;
import com.urlshortner.error.InvalidUrlError;
import com.urlshortner.service.UrlService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
//@RequestMapping("/url")
public class UrlController {

    Logger logger = LoggerFactory.getLogger(UrlController.class);

    protected final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * @param fullUrl Takes an object of FullUrl supplied in the request body
     * @param request To determine the protocol://domain:port part to form shortened url
     * @return An object of ShortUrl serialized as JSON in the response
     */
    @PostMapping("/shorten")
    public ResponseEntity<Object> saveUrl(@RequestBody FullUrl fullUrl, HttpServletRequest request) {

        // Validation checks to determine if the supplied URL is valid
        UrlValidator validator = new UrlValidator(
                new String[]{"http", "https"}
        );
        String url = fullUrl.getFullUrl();
        if (!validator.isValid(url)) {
            logger.error("Malformed Url provided");

            InvalidUrlError error = new InvalidUrlError("url", fullUrl.getFullUrl(), "Invalid URL");

            // returns a custom body with error message and bad request status code
            return ResponseEntity.badRequest().body(error);
        }
        String baseUrl = null;

        try {
            baseUrl = UrlUtil.getBaseUrl(request.getRequestURL().toString());
        } catch (MalformedURLException e) {
            logger.error("Malformed request url");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request url is invalid", e);
        }

        // Retrieving the Shortened url and concatenating with protocol://domain:port
        ShortUrl shortUrl = urlService.getShortUrl(fullUrl);
        shortUrl.setShortUrl(baseUrl + shortUrl.getShortUrl());

        logger.debug(String.format("ShortUrl for FullUrl %s is %s", fullUrl.getFullUrl(), shortUrl.getShortUrl()));

        return new ResponseEntity<>(shortUrl, HttpStatus.OK);
    }

   

}

package com.urlshortner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urlshortner.model.UrlEntity;


@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

    List<UrlEntity> findUrlByFullUrl(String fullUrl);
}

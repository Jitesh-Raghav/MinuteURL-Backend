package com.example.shortnerurl.Repository;

import com.example.shortnerurl.Entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepo extends JpaRepository<Url, Long> {

    public Url findByShortLink(String shortLink);
}

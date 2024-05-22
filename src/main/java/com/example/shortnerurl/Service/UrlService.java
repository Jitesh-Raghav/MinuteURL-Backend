package com.example.shortnerurl.Service;

import com.example.shortnerurl.Dto.UrlDto;
import com.example.shortnerurl.Entity.Url;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UrlService {

    public Url generateShortLink(UrlDto urlDto);
    public Url persistShortLink(Url url);
    public Url getEncodedUrl(String url);
    public void deleteShortLink(Url url);
    public List<Url> findAllByOrderByCreatedAtDesc();
}

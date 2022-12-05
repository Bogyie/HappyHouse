package com.ssafy.happyhouse.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.happyhouse.domain.entity.News;

@Service
public class NewsService {

    private final ObjectMapper mapper;
    private final URL url;

    public List<News> getNews() throws IOException {
        final NewsResponse response = mapper.readValue(url, NewsResponse.class);
        if (response.items != null) {
            return response.items;
        }
        return Collections.emptyList();
    }

    public NewsService() {
        mapper = new ObjectMapper();
        try {
            url = new URL("https://api.rss2json.com/v1/api.json?rss_url=http://www.renews.co.kr/rss/allArticle.xml");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NewsResponse {

        public String status;
        public Feed feed;
        public List<News> items;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Feed{
            public String url;
            public String title;
            public String link;
            public String author;
            public String description;
            public String image;
        }
    }
}

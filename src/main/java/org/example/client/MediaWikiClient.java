package org.example.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class MediaWikiClient {

    private static final String API_URL = "https://honkai-star-rail.fandom.com/api.php";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MediaWikiClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public JsonNode getPlayableCharacters() {
        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("action", "query")
                .queryParam("format", "json")
                .queryParam("list", "categorymembers")
                .queryParam("cmtitle", "Category:Playable_Characters")
                .queryParam("cmlimit", "500")
                .toUriString();

        return getJson(url);
    }

    public JsonNode getCharacterPage(String pageTitle) {
        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("action", "query")
                .queryParam("format", "json")
                .queryParam("prop", "pageimages")
                .queryParam("pithumbsize", "1200")
                .queryParam("titles", pageTitle)
                .toUriString();

        return getJson(url);
    }

    private JsonNode getJson(String url) {
        try {
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readTree(response);
        } catch (Exception e) {
            throw new RuntimeException("MediaWiki API request failed: " + url, e);
        }
    }
}

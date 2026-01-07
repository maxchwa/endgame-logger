package org.example.service;

import org.example.modal.Chara;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FandomScraper {

    public void enrichChara(Chara chara) {

        String wikiName = chara.getName().replace(" ", "_");
        String url = "https://honkai-star-rail.fandom.com/wiki/" + wikiName;

        try {

            Document doc1 = Jsoup.connect(url).userAgent("Mozilla/5.0").get();

            Element script = doc1.selectFirst("script[type=application/ld+json]");

            if (script != null) {
                String jsonText = script.html();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(jsonText);

                String imageUrl = root.get("image").asText();

                if (imageUrl.contains("?")) {
                    imageUrl = imageUrl.split("\\?")[0];
                }

                chara.setLargeImage(imageUrl);
            }

            if (chara.getName() != null && !chara.getName().isEmpty()) {

                String charNameForUrl = chara.getName().replace(" ", "_");
                charNameForUrl = charNameForUrl.replaceAll("[^A-Za-z0-9_]", "");

                String smallImageUrl = "https://starrail.honeyhunterworld.com/img/character/"
                        + charNameForUrl.toLowerCase() + "-character_icon_100.webp";

                //Unfortunately does not work for certain chars typically SPs or TB/March 7th forms

                chara.setSmallImage(smallImageUrl);

            }

            Elements infoboxItems = doc1.select("div.pi-item");

            for (Element item : infoboxItems) {

                Element valueDiv = item.selectFirst("div.pi-data-value");

                if (valueDiv == null) {
                    continue;
                }

                String label = item.selectFirst("h3.pi-data-label") != null
                        ? item.selectFirst("h3.pi-data-label").text().trim()
                        : "";

                Element img = valueDiv.selectFirst("img");

                String valueText = valueDiv.text().trim();

                if (label.equalsIgnoreCase("Rarity")) {
                    if (img != null && img.hasAttr("alt")) {
                        chara.setRarity(parseRarity(img.attr("alt")));
                    } else {
                        chara.setRarity(parseRarity(valueText));
                    }
                } else if (label.equalsIgnoreCase("Combat Path")) {
                    if (img != null && img.hasAttr("alt")) {
                        chara.setPath(img.attr("alt").replace("Path ", "").trim());
                    } else {
                        chara.setPath(valueText.replace("Path ", "").trim());
                    }
                } else if (label.equalsIgnoreCase("Combat Type")) {
                    if (img != null && img.hasAttr("alt")) {
                        chara.setElement(img.attr("alt").replace("Type ", "").trim());
                    } else {
                        chara.setElement(valueText.replace("Type ", "").trim());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int parseRarity(String text) {
        try {
            if (text != null && text.matches("\\d+.*")) {
                return Integer.parseInt(text.replaceAll("[^0-9]", ""));
            }
        } catch (Exception ignored) {
        }
        return 0;
    }
}




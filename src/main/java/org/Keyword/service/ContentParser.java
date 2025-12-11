package org.keyword.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class ContentParser {

    public String extractText(String url) {
        try {
            // Jsoup ile bağlantı kuruyoruz
            Document doc = Jsoup.connect(url)
                    // Imitate a real person
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                    .referrer("http://www.google.com")
                    // wait for maximum 10 seconds.
                    .timeout(10000) 
                    .get();

            String text = doc.body().text();

            // filter unnecessary information.
            if (text.length() < 200) {
                System.out.println("⚠ İçerik çok kısa, atlandı: " + url);
                return "";
            }
            
            return text;

        } catch (Exception e) {
            // if Error 403 or 404 returns, write on terminal and continue on with the code
            System.err.println("İçerik çekilemedi (" + url + "): " + e.getMessage());
            return "";
        }
    }
}
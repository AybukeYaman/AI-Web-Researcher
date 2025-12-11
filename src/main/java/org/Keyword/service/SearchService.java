package org.keyword.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.stereotype.Service;

import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class SearchService {

    public List<String> searchGoogle(String keyword) {
        WebDriver driver = getAvailableDriver();
        List<String> links = new ArrayList<>();

        if (driver == null) {
            System.err.println("KRİTİK HATA: Tarayıcı başlatılamadı.");
            return links;
        }

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            try { driver.manage().window().minimize(); } catch (Exception ignored) {}

            System.out.println("🔍 Bing Arama Motoru açılıyor: " + keyword);
            driver.get("https://www.bing.com/search?q=" + keyword);
            
            //give some time for results
            Thread.sleep(3000); 

            List<WebElement> results = driver.findElements(By.cssSelector("li.b_algo h2 a"));

            if (results.isEmpty()) {
                System.out.println("⚠ Standart yapı bulunamadı, geniş tarama yapılıyor...");
                results = driver.findElements(By.cssSelector("h2 a"));
            }

            System.out.println("DEBUG: Toplam " + results.size() + " adet aday başlık inceleniyor...");

            int count = 0;
            for (WebElement link : results) {
                if (count >= 5) break;

                try {
                    String rawUrl = link.getAttribute("href");
                    
                    // clean bing url
                    String cleanUrl = decodeBingUrl(rawUrl);

                    // if raw url is already usable, use it anyway
                    if (cleanUrl == null) cleanUrl = rawUrl;

                    if (cleanUrl != null && !cleanUrl.isEmpty() && cleanUrl.startsWith("http") &&
                        !cleanUrl.contains("bing.com") &&
                        !cleanUrl.contains("google.") &&
                        !cleanUrl.contains("youtube.com")) {
                        
                        if (!links.contains(cleanUrl)) {
                            links.add(cleanUrl);
                            count++;
                            System.out.println("✅ LİSTEYE EKLENDİ (" + count + "/5): " + cleanUrl);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Link işlenirken hata: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("Genel Hata: " + e.getMessage());
        } finally {
            if (driver != null) {
                try { driver.quit(); } catch (Exception ignored) {}
            }
        }

        return links;
    }

    //decode bing url
    private String decodeBingUrl(String url) {
        if (url == null || !url.contains("bing.com/ck/a")) {
            return url; // Şifreli değilse olduğu gibi döndür
        }

        try {
            // find 'u=' parameter in url
            Pattern pattern = Pattern.compile("[?&]u=([^&]+)");
            Matcher matcher = pattern.matcher(url);

            if (matcher.find()) {
                String encodedValue = matcher.group(1);
                
        // as far as I understand, bing adds "a1" when it encrypts data, this cleans it.
                if (encodedValue.startsWith("a1")) {
                    encodedValue = encodedValue.substring(2);
                }

                // decode Base64
                byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedValue);
                String realUrl = new String(decodedBytes, StandardCharsets.UTF_8);
                
                return realUrl;
            }
        } catch (Exception e) {
            System.err.println("URL Decode Hatası: " + e.getMessage());
        }
        return url; // if an error occures, return original
    }

    private WebDriver getAvailableDriver() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            try { return new SafariDriver(); } catch (Exception ignored) {}
        }
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            return new ChromeDriver(options);
        } catch (Exception ignored) {}
        try {
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver();
        } catch (Exception ignored) {}
        return null;
    }
}
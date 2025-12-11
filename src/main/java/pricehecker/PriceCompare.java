package pricehecker;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;

public class PriceCompare {

    public static void main(String[] args) {
        // WebDriver kurulumu
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));//siteye bot olmadığımı söyle
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");//ekranı fullle

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));//15 sn yüklenmesini bekle sayfanın

        try {
            System.out.println("=== SAMSUNG FİYAT KARŞILAŞTIRMASI ===\n");

            // 3 siteden fiyatları al
            double price1 = getSamsungFromNaveenLabs(driver, wait);
            double price2 = getSamsungFromDemoBlaze(driver, wait);
            double price3 = getSamsungFromWebScraper(driver, wait);

            // Rapor yazdır
            printReport(price1, price2, price3);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    // Site 1: NaveenLabs
    static double getSamsungFromNaveenLabs(WebDriver driver, WebDriverWait wait) {
        System.out.println("--- NaveenLabs OpenCart ---");

        try {
            driver.get("https://naveenautomationlabs.com/opencart/index.php?route=product/category&path=57");
            Thread.sleep(2000);//2 sn bekle thread in yüklenmesini

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-layout")));

            WebElement priceElement = driver.findElement(
                    By.xpath("//a[contains(text(), 'Samsung Galaxy Tab')]/../..//p[@class='price']")
            );
            double price = cleanPrice(priceElement.getText().split("\n")[0]);

            System.out.println("✓ Ürün: Samsung Galaxy Tab 10.1");
            System.out.println("✓ Fiyat: $" + price + "\n");
            return price;

        } catch (Exception e) {
            System.out.println("✗ Hata\n");
            return 0;
        }
    }

    // Site 2: DemoBlaze
    static double getSamsungFromDemoBlaze(WebDriver driver, WebDriverWait wait) {
        System.out.println("--- DemoBlaze ---");

        try {
            driver.get("https://www.demoblaze.com/");
            Thread.sleep(2000);

            driver.findElement(By.xpath("//a[text()='Phones']")).click();
            Thread.sleep(1500);

            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[text()='Samsung galaxy s6']/../../h5")
            ));
            double price = cleanPrice(priceElement.getText());

            System.out.println("✓ Ürün: Samsung galaxy s6");
            System.out.println("✓ Fiyat: $" + price + "\n");
            return price;

        } catch (Exception e) {
            System.out.println("✗ Hata\n");
            return 0;
        }
    }

    // Site 3: WebScraper
    static double getSamsungFromWebScraper(WebDriver driver, WebDriverWait wait) {
        System.out.println("--- WebScraper.io ---");

        try {
            driver.get("https://webscraper.io/test-sites/e-commerce/static/computers/tablets?page=2");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-wrapper")));

            WebElement priceElement = driver.findElement(
                    By.xpath("//a[@title='Galaxy Tab']/ancestor::div[contains(@class, 'product-wrapper')]//h4[@class='price float-end card-title pull-right']")
            );
            double price = cleanPrice(priceElement.getText());

            System.out.println("✓ Ürün: Galaxy Tab");
            System.out.println("✓ Fiyat: $" + price + "\n");
            return price;

        } catch (Exception e) {
            System.out.println("✗ Hata\n");
            return 0;
        }
    }

    // Fiyat temizleme
    static double cleanPrice(String text) {
        String clean = text.replaceAll("[^0-9.]", "");
        return clean.isEmpty() ? 0 : Double.parseDouble(clean);
    }

    // Rapor
    static void printReport(double p1, double p2, double p3) {
        System.out.println("==========================================");
        System.out.println("      FİYAT KARŞILAŞTIRMA RAPORU");
        System.out.println("==========================================");

        System.out.printf("NaveenLabs OpenCart    : $%.2f%n", p1);
        System.out.printf("DemoBlaze              : $%.2f%n", p2);
        System.out.printf("WebScraper.io          : $%.2f%n", p3);

        // Geçerli fiyatları bul
        double min = Double.MAX_VALUE, max = 0, sum = 0;
        int count = 0;

        if (p1 > 0) { min = Math.min(min, p1); max = Math.max(max, p1); sum += p1; count++; }
        if (p2 > 0) { min = Math.min(min, p2); max = Math.max(max, p2); sum += p2; count++; }
        if (p3 > 0) { min = Math.min(min, p3); max = Math.max(max, p3); sum += p3; count++; }

        if (count > 0) {
            double avg = sum / count;
            System.out.println("------------------------------------------");
            System.out.printf("En Ucuz            : $%.2f%n", min);
            System.out.printf("En Pahalı          : $%.2f%n", max);
            System.out.printf("Ortalama           : $%.2f%n", avg);
            System.out.printf("Fiyat Farkı        : $%.2f%n", (max - min));
            System.out.println("\n✓ BAŞARILI: Karşılaştırma tamamlandı.");
        }
        System.out.println("==========================================");
    }
}
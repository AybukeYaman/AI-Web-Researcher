package login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Lokal driver path YOK — Selenium Manager otomatik bulur
        driver = new EdgeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void validLoginTest() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();

        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains("logged-in-successfully"));
    }

    @Test
    public void invalidLoginTest() {
        driver.get("https://practicetestautomation.com/practice-test-login/");

        driver.findElement(By.id("username")).sendKeys("wrongUser");
        driver.findElement(By.id("password")).sendKeys("wrongPass");
        driver.findElement(By.id("submit")).click();

        String error = driver.findElement(By.id("error")).getText();
        Assert.assertTrue(error.contains("Your username is invalid"));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}

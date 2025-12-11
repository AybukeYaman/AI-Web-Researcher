package login;

import org.openqa.selenium.By;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;//testng için baya junit ile aynı mantık

public class LoginTest {
    WebDriver driver;
    String baseUrl = "https://the-internet.herokuapp.com/login";



    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) {

        if(browser.equals("chrome")){
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if(browser.equals("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new FirefoxDriver();
        } else if(browser.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }

        driver.manage().window().maximize(); //tarayıcıyı tam ekran yap
        driver.get(baseUrl); //login sayfasına git
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testValidLogin() {
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String successMessage = driver.findElement(By.id("flash")).getText();
        Assert.assertTrue(successMessage.contains("You logged"));

    }

    @Test
    public void testInvalidUsername(){
        driver.findElement(By.id("username")).sendKeys("wronguser");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String successMessage = driver.findElement(By.id("flash")).getText();
        Assert.assertTrue(successMessage.contains("Your username is invalid"));

    }
    @Test
    public void testInvalidPassword(){
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("WrongPassword");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String successMessage = driver.findElement(By.id("flash")).getText();
        Assert.assertTrue(successMessage.contains("Your password is invalid"));

    }


}

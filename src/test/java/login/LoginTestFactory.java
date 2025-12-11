package login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTestFactory {
    WebDriver driver;
    String baseUrl = "https://the-internet.herokuapp.com/login";

    // Factory ile parametre olarak gelecek değerler
    private String browser;
    private String username;
    private String password;
    private String expectedMessageFragment;
    private String testDescription;

    // Constructor - Factory bunları kullanacak
    public LoginTestFactory(String browser, String username, String password,
                            String expectedMessageFragment, String testDescription) {
        this.browser = browser;
        this.username = username;
        this.password = password;
        this.expectedMessageFragment = expectedMessageFragment;
        this.testDescription = testDescription;
    }

    @Factory
    public static Object[] createTestInstances() {
        return new Object[] {
                new LoginTestFactory("chrome", "tomsmith", "SuperSecretPassword!",
                        "You logged", "Chrome - Valid Login"),

                new LoginTestFactory("firefox", "tomsmith", "SuperSecretPassword!",
                        "You logged", "Firefox - Valid Login"),

                new LoginTestFactory("chrome", "wronguser", "SuperSecretPassword!",
                        "username is invalid", "Chrome - Invalid Username"),

                new LoginTestFactory("firefox", "wronguser", "SuperSecretPassword!",
                        "username is invalid", "Firefox - Invalid Username"),

                new LoginTestFactory("chrome", "tomsmith", "WrongPassword!",
                        "password is invalid", "Chrome - Invalid Password"),

                new LoginTestFactory("firefox", "tomsmith", "WrongPassword!",
                        "password is invalid", "Firefox - Invalid Password")
        };
    }

    @BeforeMethod
    public void setup() {
        if(browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if(browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }

        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

    @AfterMethod
    public void tearDown() {
        if(driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginScenario() {
        System.out.println("Running: " + testDescription);

        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String actualMessage = driver.findElement(By.id("flash")).getText();

        Assert.assertTrue(actualMessage.contains(expectedMessageFragment),
                "Test: " + testDescription + " - Expected message to contain: " + expectedMessageFragment);
    }
}
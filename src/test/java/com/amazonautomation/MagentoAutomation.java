import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class MagentoAutomation {
    WebDriver driver;

    @BeforeClass
    public void setup() {
     //   System.setProperty("webdriver.chrome.driver", "C:\\Users\\express\\Desktop\\chromedriver-win64\\chromedriver.exe");
System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://magento.softwaretestingboard.com/");
    }

    @Test(priority = 1)
    public void createAccount() {
        System.out.println("Navigating to the account creation page...");
        driver.findElement(By.linkText("Create an Account")).click();

        System.out.println("Filling out the form...");
        driver.findElement(By.id("firstname")).sendKeys("Test567843");
        driver.findElement(By.id("lastname")).sendKeys("User4545");
        driver.findElement(By.id("email_address")).sendKeys("testusreder@gmail.com");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("password-confirmation")).sendKeys("Password123");

        System.out.println("Submitting the form...");
        driver.findElement(By.cssSelector("button[title='Create an Account']")).click();

        System.out.println("Waiting for the success message...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Thank you for registering')]")));
        Assert.assertTrue(successMessage.isDisplayed(), "Account creation failed.");

        System.out.println("Navigating to the Home Page...");
        driver.findElement(By.cssSelector("a.logo")).click(); // Assuming the site logo redirects to the home page

        // Wait for home page to load completely
        WebElement homePageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[title='Home']"))); // Adjust locator if needed
        Assert.assertTrue(homePageElement.isDisplayed(), "Failed to navigate to Home Page.");
    }

    @Test(priority = 2)
    public void navigateHotSellersAndCompare() {
        System.out.println("Navigating to Hot Sellers...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement hotSellersLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Hot Sellers")));
        hotSellersLink.click();

        System.out.println("Adding first product to Compare...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[title='Add to Compare']"))).click();

        System.out.println("Adding second product to Compare...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//a[@title='Add to Compare'])[2]"))).click();

        System.out.println("Verifying products in Compare List...");
        WebElement compareProducts = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#compare-products")));
        Assert.assertTrue(compareProducts.isDisplayed(), "Products not added to compare list.");
    }
    @AfterClass
    public void teardown() {
        driver.quit();
    }
}

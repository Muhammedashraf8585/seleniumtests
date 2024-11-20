package com.amazonautomation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AmazonAutomation {
    WebDriver driver;

    public static void main(String[] args) {
        AmazonAutomation amazonAutomation = new AmazonAutomation();
        amazonAutomation.runScenario();
    }

    public void runScenario() {
        setup();

        login();
        navigateToVideoGames();
        applyFilters();
        sortByPriceHighToLow();
        addProductsBelow15kToCart();
        verifyCartItems();
        completeCheckout();

        teardown();
    }

    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\express\\Desktop\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.amazon.eg/");
    }

    public void login() {
        driver.findElement(By.id("nav-link-accountList")).click();
        driver.findElement(By.id("ap_email")).sendKeys("your_email@example.com");
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("ap_password")).sendKeys("your_password");
        driver.findElement(By.id("signInSubmit")).click();
    }

    public void navigateToVideoGames() {
        driver.findElement(By.id("nav-hamburger-menu")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement videoGamesMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Video Games']")));
        videoGamesMenu.click();

        WebElement allVideoGames = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='All Video Games']")));
        allVideoGames.click();
    }

    public void applyFilters() {
        // Apply "Free Shipping" filter
        WebElement freeShipping = driver.findElement(By.xpath("//span[text()='Free Shipping']"));
        freeShipping.click();

        // Apply "Condition: New" filter
        WebElement conditionNew = driver.findElement(By.xpath("//span[text()='New']"));
        conditionNew.click();
    }

    public void sortByPriceHighToLow() {
        WebElement sortMenu = driver.findElement(By.id("a-autoid-0-announce"));
        sortMenu.click();

        WebElement highToLow = driver.findElement(By.xpath("//a[text()='Price: High to Low']"));
        highToLow.click();
    }

    public void addProductsBelow15kToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        while (true) {
            List<WebElement> products = driver.findElements(By.cssSelector(".s-main-slot .s-result-item"));
            boolean productAdded = false;

            for (WebElement product : products) {
                try {
                    WebElement priceElement = product.findElement(By.cssSelector(".a-price-whole"));
                    String priceText = priceElement.getText().replace(",", "");
                    int price = Integer.parseInt(priceText);

                    if (price < 15000) {
                        WebElement addToCartButton = product.findElement(By.cssSelector("input[name='submit.add-to-cart']"));
                        addToCartButton.click();
                        productAdded = true;

                        // Wait for "Added to Cart" confirmation
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("huc-v2-order-row-confirm-text")));
                        driver.navigate().back();
                    }
                } catch (Exception e) {
                    // Skip products without prices or add-to-cart buttons
                }
            }

            if (!productAdded) {
                try {
                    WebElement nextPageButton = driver.findElement(By.cssSelector(".s-pagination-next"));
                    if (nextPageButton.isEnabled()) {
                        nextPageButton.click();
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    break; // No more pages
                }
            }
        }
    }

    public void verifyCartItems() {
        driver.findElement(By.id("nav-cart")).click();

        List<WebElement> cartItems = driver.findElements(By.cssSelector(".sc-list-item"));
        System.out.println("Number of items in the cart: " + cartItems.size());

        for (WebElement item : cartItems) {
            String priceText = item.findElement(By.cssSelector(".sc-product-price")).getText().replace(",", "");
            int price = Integer.parseInt(priceText);
            assert price < 15000 : "Found item in cart exceeding 15k EGP!";
        }
    }

    public void completeCheckout() {
        driver.findElement(By.cssSelector(".a-button-input")).click(); // Proceed to checkout button

        // Add address
        driver.findElement(By.id("address-ui-widgets-enterAddressFullName")).sendKeys("John Doe");
        driver.findElement(By.id("address-ui-widgets-enterAddressPhoneNumber")).sendKeys("01012345678");
        driver.findElement(By.id("address-ui-widgets-enterAddressPostalCode")).sendKeys("12345");
        driver.findElement(By.id("address-ui-widgets-enterAddressLine1")).sendKeys("123 Street Name");
        driver.findElement(By.id("address-ui-widgets-enterAddressCity")).sendKeys("Cairo");
        driver.findElement(By.cssSelector(".a-button-input")).click(); // Save address

        // Choose cash payment
        driver.findElement(By.xpath("//span[text()='Cash on Delivery']")).click();
        driver.findElement(By.cssSelector(".a-button-input")).click(); // Confirm payment

        // Validate total price
        String totalPriceText = driver.findElement(By.id("sc-subtotal-amount-activecart")).getText().replace(",", "");
        int totalPrice = Integer.parseInt(totalPriceText);
        System.out.println("Total price including shipping: " + totalPrice);
    }

    public void teardown() {
        driver.quit();
    }
}

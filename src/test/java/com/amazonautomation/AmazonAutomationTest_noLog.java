package com.amazonautomation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
//import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

public class AmazonAutomationTest_noLog {
    public static void main(String[] args) {
    //    WebDriverManager.chromedriver().browserVersion("131.0.6778.68").setup();

        // 1. Set up the ChromeDriver path (update this path if needed)
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\express\\Desktop\\chromedriver-win64\\chromedriver.exe");

        // 2. Initialize WebDriver for Chrome
        WebDriver driver = new ChromeDriver();

        // 3. Maximize the browser window for better visibility
        driver.manage().window().maximize();

        try {
            // 4. Navigate to Amazon.eg homepage
            driver.get("https://www.amazon.eg");

            // 5. Click on the "Sign-In" button
            WebElement signInButton = driver.findElement(By.id("nav-link-accountList"));
            signInButton.click();

            // 6. Enter the email address for login
            WebElement emailInput = driver.findElement(By.id("ap_email"));
            emailInput.sendKeys("your_email@example.com");

            // 7. Click "Continue" to proceed
            WebElement continueButton = driver.findElement(By.id("continue"));
            continueButton.click();

            // 8. Enter the password
            WebElement passwordInput = driver.findElement(By.id("ap_password"));
            passwordInput.sendKeys("your_password");

            // 9. Click the "Sign-In" button to log in
            WebElement loginButton = driver.findElement(By.id("signInSubmit"));
            loginButton.click();

            // 10. Open the "All" menu
            WebElement allMenu = driver.findElement(By.id("nav-hamburger-menu"));
            allMenu.click();

            // 11. Navigate to "Video Games" and then "All Video Games"
            WebElement videoGamesLink = driver.findElement(By.linkText("Video Games"));
            videoGamesLink.click();

            WebElement allVideoGamesLink = driver.findElement(By.linkText("All Video Games"));
            allVideoGamesLink.click();

            // 12. Apply the "Free Shipping" filter
            WebElement freeShippingFilter = driver.findElement(By.xpath("//span[text()='Free Shipping']"));
            freeShippingFilter.click();

            // 13. Apply the "New Condition" filter
            WebElement newConditionFilter = driver.findElement(By.xpath("//span[text()='New']"));
            newConditionFilter.click();

            // 14. Sort by "Price: High to Low"
            Select sortDropdown = new Select(driver.findElement(By.id("s-result-sort-select")));
            sortDropdown.selectByVisibleText("Price: High to Low");

            // 15. Find products priced below 15,000 EGP and add them to the cart
            List<WebElement> products = driver.findElements(By.cssSelector(".s-main-slot .s-result-item"));
            for (WebElement product : products) {
                try {
                    // Get the product price
                    String priceText = product.findElement(By.cssSelector(".a-price-whole")).getText().replace(",", "");
                    double price = Double.parseDouble(priceText);

                    if (price < 15000) {
                        // Click the product to view details
                        product.findElement(By.cssSelector(".a-link-normal")).click();

                        // Click "Add to Cart"
                        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-button"));
                        addToCartButton.click();

                        // Go back to the product list
                        driver.navigate().back();
                    }
                } catch (Exception e) {
                    System.out.println("Skipping product due to missing data.");
                }
            }

            // 16. Validate that items have been added to the cart
            WebElement cartButton = driver.findElement(By.id("nav-cart"));
            cartButton.click();

            List<WebElement> cartItems = driver.findElements(By.cssSelector(".sc-list-item"));
            if (cartItems.size() > 0) {
                System.out.println("Products successfully added to the cart!");
            } else {
                System.out.println("No products were added to the cart.");
            }

        } catch (Exception e) {
            // 17. Handle any unexpected errors
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            // 18. Close the browser
            driver.quit();
        }
    }
}


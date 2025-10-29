package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.List;

public class GoogleSearchTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.out.println("🚀 Launching Chrome browser...");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void testGoogleSearch() throws InterruptedException {
        driver.get("https://www.google.com");
        System.out.println("🌐 Navigated to Google");

        // ✅ Handle consent popup if present
        try {
            WebElement agreeButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(., 'Accept all') or contains(., 'Agree') or contains(., 'accept')]")));
            agreeButton.click();
            System.out.println("✅ Clicked on consent popup");
        } catch (Exception e) {
            System.out.println("⚠️ No consent popup detected, continuing...");
        }

        // ✅ Perform search
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
        searchBox.sendKeys("Selenium Java tutorial", Keys.RETURN);
        System.out.println("🔍 Search submitted");

        // ✅ Wait for search results
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("a h3")));

        List<WebElement> results = driver.findElements(By.cssSelector("a h3"));
        System.out.println("📄 Results found: " + results.size());
        Assert.assertTrue(results.size() > 0, "No search results found!");

        WebElement firstResult = results.get(0);

        // ✅ Scroll into view to avoid overlay click issues
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstResult);
        Thread.sleep(1000);

        // ✅ Click with JavaScript to avoid interception
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstResult);
        System.out.println("🖱️ Clicked first result safely");

        // ✅ Wait until the page title contains "Selenium"
        wait.until(ExpectedConditions.titleContains("Selenium"));
        String title = driver.getTitle();
        System.out.println("📘 Page Title: " + title);

        Assert.assertTrue(title.toLowerCase().contains("selenium"), "❌ Title does not contain 'Selenium'");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
        System.out.println("✅ Browser closed successfully.");
    }
}

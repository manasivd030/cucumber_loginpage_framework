package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class TestHooks {
    public static WebDriver driver;

    @Before
    public void setUp() throws Exception {
        // IMPORTANT: Do NOT call WebDriverManager here. Let Selenium Manager handle it.
        Path userDataDir = Files.createTempDirectory("chrome-user-data-");

        ChromeOptions opts = new ChromeOptions();
        opts.addArguments(
                // Always run headless on CI
                "--headless=new",
                // CI/Linux stability flags
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--disable-extensions",
                "--no-first-run",
                "--no-default-browser-check",
                "--password-store=basic",
                "--use-mock-keychain",
                "--window-size=1440,900",
                // Unique, throwaway profile per session
                "--user-data-dir=" + userDataDir.toAbsolutePath()
        );
        // NOTE: deliberately NOT setting --profile-directory

        // Optional: debug driver startup if needed
        System.setProperty("webdriver.chrome.logfile", "chromedriver.log");
        System.setProperty("webdriver.chrome.verboseLogging", "true");

        driver = new ChromeDriver(opts);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().window().setSize(new Dimension(1440, 900));
    }

    @After
    public void tearDown(io.cucumber.java.Scenario scenario) {
        if (scenario.isFailed() && driver instanceof TakesScreenshot ts) {
            attachPng(ts.getScreenshotAs(OutputType.BYTES));
        }
        if (driver != null) driver.quit();
    }

    @Attachment(value = "Failure screenshot", type = "image/png")
    public byte[] attachPng(byte[] bytes) { return bytes; }
}

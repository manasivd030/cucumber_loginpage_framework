package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.qameta.allure.Attachment;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import config.Config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;

public class TestHooks {
    public static WebDriver driver;

    @Before
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();

        // Create a unique, throwaway Chrome profile per session
        Path userDataDir = Files.createTempDirectory("chrome-user-data-");
        String profileDir = "Profile-" + UUID.randomUUID();

        ChromeOptions opts = new ChromeOptions();

        // Always include the stable CI flags (even if not headless)
        opts.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--disable-extensions",
                "--window-size=1440,900"
        );

        if (Config.headless()) {
            // Use modern headless
            opts.addArguments("--headless=new");
        }

        // Unique profile prevents "user data directory is already in use"
        opts.addArguments("--user-data-dir=" + userDataDir.toAbsolutePath());
        opts.addArguments("--profile-directory=" + profileDir);

        // Optional: helps on some runners / versions
        // opts.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(opts);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().window().setSize(new Dimension(1440, 900));
    }

    @After
    public void tearDown(io.cucumber.java.Scenario scenario) {
        if (scenario.isFailed() && driver instanceof TakesScreenshot) {
            attachPng(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        }
        if (driver != null) {
            driver.quit(); // releases the profile lock
        }
    }

    @Attachment(value = "Failure screenshot", type = "image/png")
    public byte[] attachPng(byte[] bytes) { return bytes; }
}

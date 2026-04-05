package tests;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeMethod;

import java.util.Map;

public class MobileBaseTest extends BaseTest {

    @BeforeMethod
    @Override
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("mobileEmulation", Map.of(
                "deviceName", "iPhone 12 Pro"
        ));

        driver = new ChromeDriver(options);
    }
}

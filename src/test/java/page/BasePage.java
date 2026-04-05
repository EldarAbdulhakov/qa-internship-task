package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import utils.WaiterUtils;

public class BasePage {

    private final WebDriver driver;
    protected WaiterUtils waiter;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waiter = new WaiterUtils(driver);
        PageFactory.initElements(driver, this);
    }

    protected WebDriver getDriver() {
        return driver;
    }
}

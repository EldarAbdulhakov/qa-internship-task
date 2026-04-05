package page;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class StatsPage extends BasePage {

    @FindBy(css = "[class*='refreshButton']")
    private WebElement refreshButton;

    @FindBy(css = "[title='Отключить автообновление']")
    private WebElement toggleButtonActive;

    @FindBy(css = "[title='Включить автообновление']")
    private WebElement toggleButtonInactive;

    @FindBy(css = "[class*='timeValue']")
    private WebElement timer;

    public StatsPage(WebDriver driver) {
        super(driver);
    }

    public StatsPage clickRefreshButton() {
        waiter.getWait10().until(ExpectedConditions.elementToBeClickable(refreshButton));
        refreshButton.click();

        return this;
    }

    public StatsPage clickStopTimer() {
        waiter.getWait10().until(ExpectedConditions.elementToBeClickable(toggleButtonActive));
        toggleButtonActive.click();

        return this;
    }

    public StatsPage clickStartTimer() {
        waiter.getWait10().until(ExpectedConditions.elementToBeClickable(toggleButtonInactive));
        toggleButtonInactive.click();

        return this;
    }

    public boolean isTimerVisible() {
        try {
            return timer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getTimerValue() {
        try {
            waiter.getWait10().until(ExpectedConditions.visibilityOf(timer));
            return timer.getText();
        } catch (Exception e) {
            System.out.println("Таймер не отображается");
            return null;
        }
    }

    public StatsPage expectForTimerChanged() {
        String oldValue = getTimerValue();

        try {
            waiter.getWait5().until(d -> {
                String current = getTimerValue();
                return current != null && !current.equals(oldValue);
            });
        } catch (TimeoutException e) {
            Assert.fail("Таймер не изменился за 5 секунд");
        }

        return this;
    }
}

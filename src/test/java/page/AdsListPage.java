package page;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.List;

public class AdsListPage extends BasePage {

    @FindBy(xpath = "//input[@placeholder='От']")
    private WebElement priceFromInput;

    @FindBy(xpath = "//input[@placeholder='До']")
    private WebElement priceToInput;

    @FindBy(css = "div[class^='_cards_'] > div")
    private List<WebElement> cards;

    @FindBy(css = "div[class*='card__price_']")
    private List<WebElement> prices;

    private final By loader = By.cssSelector("div[class*='fetchingIndicator']");

    @FindBy(css = "div[class*='card__category']")
    private List<WebElement> categories;

    @FindBy(css = "div[class*='_empty_']")
    private WebElement emptyMessage;

    @FindBy(xpath = "//label[text()='Сортировать по']/following-sibling::select")
    private WebElement sortSelect;

    @FindBy(xpath = "//label[text()='Порядок']/following-sibling::select")
    private WebElement sortOrderSelect;

    @FindBy(xpath = "//label[text()='Категория']/following-sibling::select")
    private WebElement categorySelect;

    @FindBy(css = "[class*='urgentToggle__slider']")
    private WebElement urgentToggle;

    @FindBy(css = "[class*='card__priority']")
    private List<WebElement> urgentBadges;

    @FindBy(xpath = "//span[text()='Статистика']")
    private WebElement statisticButton;

    @FindBy(css = "[class*='themeToggle']")
    private WebElement themeToggle;

    @FindBy(tagName = "html")
    private WebElement html;

    public AdsListPage(WebDriver driver) {
        super(driver);
        driver.get("https://cerulean-praline-8e5aa6.netlify.app/");
    }

    public AdsListPage fillPriceFromInput(String value) {
        waiter.getWait5().until(ExpectedConditions.elementToBeClickable(priceFromInput)).clear();
        priceFromInput.sendKeys(value);

        return this;
    }

    public AdsListPage fillPriceToInput(String value) {
        waiter.getWait5().until(ExpectedConditions.elementToBeClickable(priceToInput)).clear();
        priceToInput.sendKeys(value);

        return this;
    }

    public List<Integer> getCardsPrices() {
        List<WebElement> elements = waiter.getWait10()
                .until(ExpectedConditions.visibilityOfAllElements(prices));

        return elements.stream()
                .map(el -> el.getText().replaceAll("[^0-9]", ""))
                .map(Integer::parseInt)
                .toList();
    }

    public AdsListPage waitForLoader() {
        try {
            waiter.getWait2().until(d -> !d.findElements(loader).isEmpty());
        } catch (TimeoutException ignored) {
        }

        waiter.getWait5().until(d -> d.findElements(loader).isEmpty());

        return this;
    }

    public int getCardsCount() {
        waiter.getWait10().until(ExpectedConditions.visibilityOfAllElements(cards));

        return cards.size();
    }

    public String getPriceFromValue() {
        return priceFromInput.getAttribute("value");
    }

    public String getPriceToValue() {
        return priceToInput.getAttribute("value");
    }

    public String getEmptyMessage() {
        try {
            return waiter.getWait5().until(
                    ExpectedConditions.visibilityOf(emptyMessage)
            ).getText();
        } catch (TimeoutException e) {
            return "";
        }
    }

    public AdsListPage selectSort(String value) {
        waiter.getWait10().until(ExpectedConditions.elementToBeClickable(sortSelect));
        new Select(sortSelect).selectByValue(value);

        return this;
    }

    public AdsListPage selectSortOrder(String value) {
        waiter.getWait10().until(ExpectedConditions.elementToBeClickable(sortOrderSelect));
        new Select(sortOrderSelect).selectByValue(value);

        return this;
    }

    public AdsListPage confirmDescOrder() {
        Select orderSelect = new Select(sortOrderSelect);
        String selectedText = orderSelect.getFirstSelectedOption().getText();

        Assert.assertEquals(selectedText, "По убыванию",
                "Порядок сортировки по умолчанию установлен 'По возрастанию' ");

        return this;
    }

    public AdsListPage selectCategory(String text) {
        waiter.getWait10().until(ExpectedConditions.elementToBeClickable(categorySelect));
        new Select(categorySelect).selectByVisibleText(text);

        return this;
    }

    public List<String> getCardsCategories() {
        waiter.getWait10().until(ExpectedConditions.visibilityOfAllElements(cards));

        return categories.stream()
                .map(WebElement::getText)
                .toList();
    }

    public AdsListPage clickUrgentFilter() {
        waiter.getWait10().until(ExpectedConditions.elementToBeClickable(urgentToggle));
        urgentToggle.click();

        return this;
    }

    public int getUrgentBadgeCount() {
        waiter.getWait10().until(ExpectedConditions.visibilityOf(urgentToggle));

        return urgentBadges.size();
    }

    public StatsPage goStatisticPage() {
        waiter.getWait10().until(ExpectedConditions.elementToBeClickable(urgentToggle));
        statisticButton.click();

        return new StatsPage(getDriver());
    }

    public AdsListPage clickThemeToggle() {
        waiter.getWait10().until(ExpectedConditions.elementToBeClickable(themeToggle));
        themeToggle.click();

        return this;
    }

    public String getCurrentTheme() {
        waiter.getWait10().until(ExpectedConditions.visibilityOf(themeToggle));

        return html.getAttribute("data-theme");
    }

    public AdsListPage waitForThemeChanged(String oldTheme) {
        waiter.getWait2().until(d -> {
            String newTheme = getCurrentTheme();
            return newTheme != null && !newTheme.equals(oldTheme);
        });

        return this;
    }

    public AdsListPage expectTheme(String theme) {
        String initialTheme = getCurrentTheme();

        Assert.assertEquals(initialTheme, theme,
                "Тема должна быть " + theme);

        return this;
    }
}

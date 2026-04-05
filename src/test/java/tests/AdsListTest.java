package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AdsListPage;

import java.util.Comparator;
import java.util.List;

public class AdsListTest extends BaseTest {

    @Test(description = "TC-01. Фильтрация по нижней границе цены")
    public void testFromValue() {
        int priceFrom = 30000;

        AdsListPage adsListPage = new AdsListPage(getDriver());

        List<Integer> beforeFilterPrices = adsListPage
                .getCardsPrices();

        List<Integer> expectedRemainingPrices = beforeFilterPrices.stream()
                .filter(p -> p >= priceFrom)
                .toList();

        List<Integer> actualPrices = adsListPage
                .fillPriceFromInput(String.valueOf(priceFrom))
                .waitForLoader()
                .getCardsPrices();

        Assert.assertTrue(actualPrices.stream().allMatch(p -> p >= priceFrom),
                "Отображаются объявления с ценой ниже фильтра");

        Assert.assertTrue(actualPrices.containsAll(expectedRemainingPrices),
                "Не отображаются подходящие объявления после фильтра 'От " + priceFrom + "'");
    }

    @Test(description = "TC-02. Фильтрация по верхней границе цены")
    public void testToValue() {
        int priceTo = 50000;

        AdsListPage adsListPage = new AdsListPage(getDriver());

        List<Integer> beforeFilterPrices = adsListPage
                .getCardsPrices();

        List<Integer> expectedRemainingPrices = beforeFilterPrices.stream()
                .filter(p -> p <= priceTo)
                .toList();

        List<Integer> actualPrices = adsListPage
                .fillPriceFromInput(String.valueOf(priceTo))
                .waitForLoader()
                .getCardsPrices();

        Assert.assertTrue(actualPrices.stream().allMatch(p -> p <= priceTo),
                "Отображаются объявления с ценой выше фильтра 'До " + priceTo + "'");

        Assert.assertTrue(actualPrices.containsAll(expectedRemainingPrices),
                "Не отображаются подходящие объявления после фильтра 'До " + priceTo + "'");
    }

    @Test(description = "TC-03. Фильтрация по валидному диапазону цен")
    public void testPriceRangeFilter() {
        int priceFrom = 10000;
        int priceTo = 50000;

        AdsListPage adsListPage = new AdsListPage(getDriver());

        List<Integer> beforeFilterPrices = adsListPage.getCardsPrices();

        List<Integer> expectedRemainingPrices = beforeFilterPrices.stream()
                .filter(p -> p >= priceFrom && p <= priceTo)
                .toList();

        List<Integer> actualPrices = adsListPage
                .fillPriceFromInput(String.valueOf(priceFrom))
                .fillPriceToInput(String.valueOf(priceTo))
                .waitForLoader()
                .getCardsPrices();

        Assert.assertTrue(
                actualPrices.stream().allMatch(p -> p >= priceFrom && p <= priceTo),
                "Отображаются объявления с ценами вне диапазона: от " + priceFrom + " до " + priceTo);

        Assert.assertTrue(actualPrices.containsAll(expectedRemainingPrices),
                "Не отображаются объявления с ценами в диапазоне " + priceFrom + " - " + priceTo);
    }

    @Test(description = "TC-04. Ввод значения 0 в поле фильтра цены 'От'")
    public void testPriceFromZeroInput() {
        String actualValue = new AdsListPage(getDriver())
                .fillPriceFromInput("0")
                .getPriceFromValue();

        Assert.assertEquals(actualValue, "",
                "Поле 'От' не должно принимать 0");
    }

    @Test(description = "TC-05. Ввод значения 0 в поле фильтра цены 'До'")
    public void testPriceToZeroInput() {
        String actualValue = new AdsListPage(getDriver())
                .fillPriceToInput("0")
                .getPriceToValue();

        Assert.assertEquals(actualValue, "",
                "Поле 'До' не должно принимать 0");
    }

    @Test(description = "TC-06. Ввод отрицательного числа в поле фильтра цены 'От'")
    public void testPriceFromNegativeInput() {
        String actualValue = new AdsListPage(getDriver())
                .fillPriceFromInput("-1")
                .getPriceFromValue();

        Assert.assertEquals(actualValue, "",
                "Поле 'От' не должно принимать отрицательное число");
    }

    @Test(description = "TC-07. Ввод отрицательного числа в поле фильтра цены 'До'")
    public void testPriceToNegativeInput() {
        String actualValue = new AdsListPage(getDriver())
                .fillPriceToInput("-1")
                .getPriceToValue();

        Assert.assertEquals(actualValue, "",
                "Поле 'До' не должно принимать отрицательное число");
    }

    @Test(description = "TC-08. Ввод значения 'От' меньше значения 'До' в фильтре цены")
    public void testPriceRangeFromLessThanTo() {
        int priceFrom = 10000;
        int priceTo = 500;

        String emptyTitle = new AdsListPage(getDriver())
                .fillPriceFromInput(String.valueOf(priceFrom))
                .fillPriceToInput(String.valueOf(priceTo))
                .getEmptyMessage();

        Assert.assertTrue(emptyTitle.contains("Объявления не найдены"),
                "Сообщения 'Объявления не найдены' не отобразилось");
    }

    @Test(description = "TC-09. Сортировка по цене по убыванию")
    public void testSortByPriceDescending() {
        List<Integer> actualSorted = new AdsListPage(getDriver())
                .selectSort("price")
                .confirmDescOrder()
                .waitForLoader()
                .getCardsPrices();

        List<Integer> expectedSorted = actualSorted.stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        Assert.assertEquals(actualSorted, expectedSorted,
                "Сортировка по цене, по убыванию работает некорректно. Получено: " + actualSorted);
    }

    @Test(description = "TC-10. Сортировка по цене по возрастанию")
    public void testSortByPriceAscending() {
        List<Integer> actualSorted = new AdsListPage(getDriver())
                .selectSort("price")
                .selectSortOrder("asc")
                .waitForLoader()
                .getCardsPrices();

        List<Integer> expectedSorted = actualSorted.stream()
                .sorted()
                .toList();

        Assert.assertEquals(actualSorted, expectedSorted,
                "Сортировка по цене, по возрастанию работает некорректно. Получено: " + actualSorted);
    }

    @DataProvider(name = "categories")
    public Object[][] categories() {
        return new Object[][]{
                {"Электроника"},
                {"Недвижимость"},
                {"Транспорт"},
                {"Работа"},
                {"Услуги"},
                {"Животные"},
                {"Мода"},
                {"Детское"}
        };
    }

    @Test(description = "TC-11. Перебор всех категорий", dataProvider = "categories")
    public void testFilterByElectronics(String categoryName) {
        List<String> actualCategories = new AdsListPage(getDriver())
                .selectCategory(categoryName)
                .waitForLoader()
                .getCardsCategories();

        Assert.assertTrue(actualCategories.stream().allMatch(c -> c.equals(categoryName)),
                "Есть карточки не из категории '" + categoryName + "': " + actualCategories);
    }

    @Test(description = "TC-12. Выбор категории 'Все' после выбора категории")
    public void testCategoryResetToAll() {
        List<String> initialCategories = new AdsListPage(getDriver())
                .getCardsCategories();

        List<String> filteredCategories = new AdsListPage(getDriver())
                .selectCategory("Электроника")
                .waitForLoader()
                .getCardsCategories();

        List<String> resetCategories = new AdsListPage(getDriver())
                .selectCategory("Все категории")
                .waitForLoader()
                .getCardsCategories();

        Assert.assertNotEquals(filteredCategories, resetCategories,
                "Фильтр категории 'Все' не применился");

        Assert.assertEquals(resetCategories, initialCategories,
                "После сброса категории список не восстановился");
    }

    @Test(description = "TC-13. Включение тогла 'Только срочные'")
    public void testUrgentToggleOn() {
        AdsListPage adsListPage = new AdsListPage(getDriver());

        int urgentBadgeCount = adsListPage
                .clickUrgentFilter()
                .waitForLoader()
                .getUrgentBadgeCount();

        int CardsCount = adsListPage.getCardsCount();

        Assert.assertEquals(CardsCount, urgentBadgeCount,
                "Кол-во объявлений: " + CardsCount + " не совпадает с кол-вом бейджей 'Срочно': " + urgentBadgeCount);
    }

    @Test(description = "TC-14. Двойной клик тогла 'Только срочные' возвращает исходные объявления")
    public void testUrgentToggleReturnsToInitialState() {
        AdsListPage adsListPage = new AdsListPage(getDriver());

        int initialCount = adsListPage.getCardsCount();

        int countAfterDisableBadge = adsListPage
                .clickUrgentFilter()
                .waitForLoader()
                .clickUrgentFilter()
                .waitForLoader()
                .getCardsCount();

        Assert.assertEquals(countAfterDisableBadge, initialCount,
                "После выключения фильтра список не восстановился");
    }
}

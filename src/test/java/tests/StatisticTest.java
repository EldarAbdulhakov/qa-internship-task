package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import page.AdsListPage;
import page.StatsPage;

public class StatisticTest extends BaseTest {

    @Test(description = "TC-15. Сброс таймера обновления статистики при нажатии кнопки 'Обновить'")
    public void testRefreshTimerButton() {
        StatsPage statsPage = new AdsListPage(getDriver())
                .goStatisticPage();

        String afterRefreshTime = statsPage
                .expectForTimerChanged()
                .clickRefreshButton()
                .getTimerValue();

        statsPage.expectForTimerChanged();

        Assert.assertTrue(afterRefreshTime.startsWith("5:") || afterRefreshTime.startsWith("4:5"),
                "Таймер обновления статистики не сбросился к 5 минутам: " + afterRefreshTime);
    }

    @Test(description = "TC-16. Нажатие на кнопку отключения автообновления прекращает отображение таймера обновления статистики")
    public void testStopAutoUpdateTimer() {
        boolean isTimerVisible = new AdsListPage(getDriver())
                .goStatisticPage()
                .clickStopTimer()
                .isTimerVisible();

        Assert.assertFalse(isTimerVisible,
                "Таймер обновления статистики отображается после отключения автообновления");
    }

    @Test(description = "TC-17. Нажатие на кнопку включения автообновления отображает таймер обновления статистики")
    public void testStartAutoUpdateTimerVisible() {
        boolean isTimerVisible = new AdsListPage(getDriver())
                .goStatisticPage()
                .clickStopTimer()
                .clickStartTimer()
                .isTimerVisible();

        Assert.assertTrue(isTimerVisible,
                "Таймер обновления статистики не отображается после включения автообновления");
    }
}

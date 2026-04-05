package tests;

import org.testng.annotations.Test;
import page.AdsListPage;

public class AdsListMobileTest extends MobileBaseTest {

    @Test(description = "TC-18. Переключение темы со светлой на темную на мобильной версии сайта")
    public void testSwitchToDarkTheme() {
        new AdsListPage(getDriver())
                .expectTheme("light")
                .clickThemeToggle()
                .expectTheme("dark");
    }

    @Test(description = "TC-19. Переключение темы с темной на светлую на мобильной версии сайта")
    public void testSwitchToLightTheme() {
        new AdsListPage(getDriver())
                .expectTheme("light")
                .clickThemeToggle()
                .expectTheme("dark")
                .clickThemeToggle()
                .expectTheme("light");
    }
}

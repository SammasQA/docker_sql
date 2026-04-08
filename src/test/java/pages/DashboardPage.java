package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private final SelenideElement dashboard = $("[data-test-id='dashboard']");

    public void shouldBeVisible() {
        dashboard.shouldBe(visible)
                .shouldHave(text("Личный кабинет"));
    }

    public void shouldNotBeVisible() {
        dashboard.shouldNotBe(visible);
    }
}
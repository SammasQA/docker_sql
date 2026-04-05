package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement loginField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");


    private void fillLoginForm(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
    }


    public VerificationPage validLogin(String login, String password) {
        fillLoginForm(login, password);
        loginButton.click();
        errorNotification.shouldNotBe(visible);
        return new VerificationPage();
    }


    public void login(String login, String password) {
        fillLoginForm(login, password);
        loginButton.click();
    }


    public void checkErrorNotificationVisible() {
        errorNotification.shouldBe(visible);
    }


    public void checkErrorNotificationText(String expectedText) {
        errorNotification.shouldBe(visible).shouldHave(text(expectedText));
    }


    public void errorNotificationShouldNotBeVisible() {
        errorNotification.shouldNotBe(visible);
    }
}
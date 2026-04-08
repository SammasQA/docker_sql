package pages;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.*;
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


    public void checkErrorNotificationText(String expectedText) {
        errorNotification.shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text(expectedText));
    }
}
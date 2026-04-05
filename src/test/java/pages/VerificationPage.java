package pages;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private final SelenideElement codeField = $("input[name='code']");
    private final SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public void verify(String code) {
        codeField.shouldBe(visible, Duration.ofSeconds(15)).setValue(code);
        verifyButton.click();
    }
}
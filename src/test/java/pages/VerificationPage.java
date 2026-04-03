package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;

public class VerificationPage {


    private final SelenideElement codeField = $("[data-test-id='code']");
    private final SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public void verify(String code) {
        codeField.shouldBe(visible).setValue(code);
        verifyButton.click();
    }
}
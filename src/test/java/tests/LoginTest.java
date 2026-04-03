package tests;

import com.codeborne.selenide.Configuration;
import helpers.DataHelper;
import pages.LoginPage;
import utils.AppStarter;
import utils.DbUtils;
import org.junit.jupiter.api.*;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTest {

    @BeforeAll
    void startApp() {

        DbUtils.cleanDatabaseForSut();


        AppStarter.start();


        Configuration.browser = "chrome";
        Configuration.holdBrowserOpen = false;
        Configuration.baseUrl = "http://localhost:9999";
    }

    @BeforeEach
    void cleanData() {

        DbUtils.cleanDataForTests();
    }

    @AfterAll
    void stopApp() {
        AppStarter.stop();
    }

    @Test
    void shouldLoginSuccessfully() {
        open("/");
        LoginPage loginPage = new LoginPage();
        var user = DataHelper.getValidUser();

        var verificationPage = loginPage.validLogin(user.getLogin(), user.getPassword());

        String code = DataHelper.getVerificationCodeFor(user.getLogin());
        verificationPage.verify(code);


        $("[data-test-id='dashboard']").shouldBe(visible);
    }

    @Test
    void shouldBlockAfterThreeInvalidPasswordAttempts() {
        open("/");
        LoginPage loginPage = new LoginPage();
        var user = DataHelper.getValidUser();


        for (int i = 0; i < 3; i++) {
            loginPage.login(user.getLogin(), "wrong_password");
            $("[data-test-id='error-notification']").shouldBe(visible);
        }


        loginPage.login(user.getLogin(), user.getPassword());

        // Проверяем, что остались на странице входа (дашборд не появился)
        $("[data-test-id='dashboard']").shouldNotBe(visible);
        // И появилось сообщение об ошибке (блокировка)
        $("[data-test-id='error-notification']").shouldBe(visible);
    }
}
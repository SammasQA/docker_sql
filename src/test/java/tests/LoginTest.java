package tests;

import com.codeborne.selenide.Configuration;
import helpers.DataHelper;
import pages.DashboardPage;
import pages.LoginPage;
import utils.AppStarter;
import utils.DbUtils;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.open;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTest {

    @BeforeAll
    void prepare() {
        DbUtils.cleanAllTables();
        AppStarter.start();
        Configuration.baseUrl = "http://localhost:9999";
       ;
    }

    @AfterEach
    void cleanUpAfterTest() {
        DbUtils.cleanTempTables();
    }

    @AfterAll
    void stopAndClean() {
        AppStarter.stop();
        DbUtils.cleanAllTables();
    }

    @Test
    void shouldLoginSuccessfully() {
        open("/");
        LoginPage loginPage = new LoginPage();
        var user = DataHelper.getValidUser();

        var verificationPage = loginPage.validLogin(user.getLogin(), user.getPassword());
        String code = DataHelper.getVerificationCodeFor(user.getLogin());
        verificationPage.verify(code);

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.shouldBeVisible();
    }

    @Test
    void shouldBlockAfterThreeInvalidPasswordAttempts() {
        open("/");
        LoginPage loginPage = new LoginPage();
        var user = DataHelper.getValidUser();
        String invalidPassword = DataHelper.getInvalidPassword();

        for (int i = 0; i < 3; i++) {
            loginPage.login(user.getLogin(), invalidPassword);
            loginPage.checkErrorNotificationText("Ошибка! Неверно указан логин или пароль");
        }

        loginPage.login(user.getLogin(), user.getPassword());

        DashboardPage dashboardPage = new DashboardPage();

        dashboardPage.shouldNotBeVisible();
    }
}
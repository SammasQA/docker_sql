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

    private DataHelper.AuthInfo testUser;

    @BeforeAll
    void prepare() {
        DbUtils.cleanAllTables();
        AppStarter.start();
        Configuration.baseUrl = "http://localhost:9999";
    }

    @BeforeEach
    void createUser() {
        testUser = DataHelper.generateRandomUser();
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
        var verificationPage = loginPage.validLogin(testUser.getLogin(), testUser.getPassword());
        String code = DataHelper.getVerificationCodeFor(testUser.getLogin());
        verificationPage.verify(code);

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.shouldBeVisible();
    }

    @Test
    void shouldBlockAfterThreeInvalidPasswordAttempts() {
        open("/");
        LoginPage loginPage = new LoginPage();
        String invalidPassword = DataHelper.getInvalidPassword();

        for (int i = 0; i < 3; i++) {
            loginPage.login(testUser.getLogin(), invalidPassword);
            loginPage.checkErrorNotificationText("Ошибка! Неверно указан логин или пароль");
        }

        loginPage.login(testUser.getLogin(), testUser.getPassword());

        loginPage.shouldBeVisible();

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.shouldNotBeVisible();
    }
}
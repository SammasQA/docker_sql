package helpers;

import lombok.Value;
import net.datafaker.Faker;

public class DataHelper {

    private static final Faker faker = new Faker();

    private DataHelper() {}

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }


    public static AuthInfo getValidUser() {
        return new AuthInfo("vasya", "password");
    }

    // Генерируем случайный неверный пароль
    public static String getInvalidPassword() {
        return faker.internet().password(8, 12) + "_invalid";
    }

    public static String getVerificationCodeFor(String login) {
        return utils.DbUtils.getVerificationCodeForUser(login);
    }
}
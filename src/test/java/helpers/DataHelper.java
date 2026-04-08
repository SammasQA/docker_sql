package helpers;

import lombok.Value;
import net.datafaker.Faker;
import org.mindrot.jbcrypt.BCrypt;
import utils.DbUtils;

public class DataHelper {
    private static final Faker faker = new Faker();

    private DataHelper() {}

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }


    public static AuthInfo generateRandomUser() {
        String login = faker.name().username();
        String rawPassword = faker.internet().password(6, 10);
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        DbUtils.insertUser(login, hashedPassword);
        return new AuthInfo(login, rawPassword);
    }


    public static String getInvalidPassword() {
        return faker.internet().password(8, 12) + "_invalid";
    }


    public static String getVerificationCodeFor(String login) {
        return DbUtils.getVerificationCodeForUser(login);
    }
}
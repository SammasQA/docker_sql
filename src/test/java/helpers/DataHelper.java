package helpers;

import utils.DbUtils;

public class DataHelper {

    private DataHelper() {}

    public static class AuthInfo {
        private final String login;
        private final String password;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    public static AuthInfo getValidUser() {
        return new AuthInfo("vasya", "password");
    }

    public static String getInvalidPassword() {
        return "wrong_password";
    }

    public static String getVerificationCodeFor(String login) {
        return DbUtils.getVerificationCodeForUser(login);
    }
}
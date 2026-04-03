package helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.DbUtils;

public class DataHelper {

    private DataHelper() {}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthInfo {
        private String login;
        private String password;
    }


    public static AuthInfo getValidUser() {

        return new AuthInfo("vasya", "password");
    }


    public static String getVerificationCodeFor(String login) {
        return DbUtils.getVerificationCodeForUser(login);
    }
}
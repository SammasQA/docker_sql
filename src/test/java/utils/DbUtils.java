package utils;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/app?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "app";
    private static final String PASSWORD = "pass";


    @SneakyThrows
    public static void cleanDatabaseForSut() {
        var runner = new QueryRunner();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            runner.update(conn, "DELETE FROM card_transactions");
            runner.update(conn, "DELETE FROM auth_codes");
            runner.update(conn, "DELETE FROM cards");
            runner.update(conn, "DELETE FROM users");
        }
    }


    @SneakyThrows
    public static void cleanDataForTests() {
        var runner = new QueryRunner();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            runner.update(conn, "DELETE FROM card_transactions");
            runner.update(conn, "DELETE FROM auth_codes");
        }
    }

    @SneakyThrows
    public static String getVerificationCodeForUser(String login) {
        var runner = new QueryRunner();
        String sql = "SELECT code FROM auth_codes WHERE user_id = (SELECT id FROM users WHERE login = ?) ORDER BY created DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return runner.query(conn, sql, new ScalarHandler<>(), login);
        }
    }
}
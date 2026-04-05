package utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtils {

    private static final String URL = "jdbc:mysql://localhost:3306/app?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "app";
    private static final String PASSWORD = "pass";

    public static void cleanAllTables() {
        try {
            var runner = new QueryRunner();
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                runner.update(conn, "DELETE FROM card_transactions");
                runner.update(conn, "DELETE FROM auth_codes");
                runner.update(conn, "DELETE FROM cards");
                runner.update(conn, "DELETE FROM users");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean all tables", e);
        }
    }

    public static void cleanTempTables() {
        try {
            var runner = new QueryRunner();
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                runner.update(conn, "DELETE FROM card_transactions");
                runner.update(conn, "DELETE FROM auth_codes");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean temp tables", e);
        }
    }

    public static String getVerificationCodeForUser(String login) {
        try {
            var runner = new QueryRunner();
            String sql = "SELECT code FROM auth_codes WHERE user_id = (SELECT id FROM users WHERE login = ?) ORDER BY created DESC LIMIT 1";
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String code = null;
                for (int i = 0; i < 10; i++) {
                    code = runner.query(conn, sql, new ScalarHandler<>(), login);
                    if (code != null) break;
                    Thread.sleep(1000);
                }
                return code;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get verification code for user: " + login, e);
        }
    }
}
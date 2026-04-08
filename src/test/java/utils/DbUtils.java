package utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public class DbUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/app?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "app";
    private static final String PASSWORD = "pass";
    private static final QueryRunner runner = new QueryRunner();

    public static void cleanAllTables() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            runner.update(conn, "DELETE FROM card_transactions");
            runner.update(conn, "DELETE FROM auth_codes");
            runner.update(conn, "DELETE FROM cards");
            runner.update(conn, "DELETE FROM users");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clean tables", e);
        }
    }

    public static void cleanTempTables() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            runner.update(conn, "DELETE FROM card_transactions");
            runner.update(conn, "DELETE FROM auth_codes");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clean temp tables", e);
        }
    }

    public static void insertUser(String login, String hashedPassword) {
        String sql = "INSERT INTO users (id, login, password, status) VALUES (?, ?, ?, 'active')";
        String uuid = UUID.randomUUID().toString();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            runner.update(conn, sql, uuid, login, hashedPassword);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert user: " + login, e);
        }
    }

    public static String getVerificationCodeForUser(String login) {
        String sql = "SELECT code FROM auth_codes WHERE user_id = (SELECT id FROM users WHERE login = ?) ORDER BY created DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            for (int i = 0; i < 10; i++) {
                String code = runner.query(conn, sql, new ScalarHandler<>(), login);
                if (code != null) return code;
                Thread.sleep(1000);
            }
            throw new RuntimeException("Verification code not found for user: " + login);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
package utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class AppStarter {
    private static Process process;

    public static void start() {
        try {
            if (process != null && process.isAlive()) {
                process.destroyForcibly();
                process.waitFor(5, TimeUnit.SECONDS);
            }
            String jarPath = "artifacts/app-deadline.jar";
            File jarFile = new File(jarPath);
            if (!jarFile.exists()) {
                throw new RuntimeException("JAR not found at: " + jarFile.getAbsolutePath());
            }
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar", jarPath,
                    "-P:profile=test",
                    "-P:jdbc.url=jdbc:mysql://localhost:3306/app",
                    "-P:jdbc.user=app",
                    "-P:jdbc.password=pass"
            );
            pb.directory(new File("."));
            pb.redirectErrorStream(true);
            pb.inheritIO();
            process = pb.start();
            Thread.sleep(15000); // ожидание запуска
        } catch (Exception e) {
            throw new RuntimeException("Failed to start application", e);
        }
    }

    public static void stop() {
        if (process != null && process.isAlive()) {
            process.destroyForcibly();
        }
    }
}
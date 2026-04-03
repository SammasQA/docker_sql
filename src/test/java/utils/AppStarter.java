package utils;

import lombok.SneakyThrows;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class AppStarter {
    private static Process process;

    @SneakyThrows
    public static void start() {
        if (process != null && process.isAlive()) {
            process.destroyForcibly();
            process.waitFor(5, TimeUnit.SECONDS);
        }

        String jarPath = "artifacts/app-deadline.jar";
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            throw new RuntimeException("JAR not found at: " + jarFile.getAbsolutePath() +
                    "\nMake sure artifacts/app-deadline.jar exists in project root.");
        }

        ProcessBuilder pb = new ProcessBuilder(
                "java", "-jar", jarPath,
                "-P:jdbc.url=jdbc:mysql://localhost:3306/app",
                "-P:jdbc.user=app",
                "-P:jdbc.password=pass"
        );
        pb.directory(new File("."));
        pb.inheritIO();
        process = pb.start();

        // Ждём запуска SUT
        Thread.sleep(15000);
    }

    @SneakyThrows
    public static void stop() {
        if (process != null && process.isAlive()) {
            process.destroyForcibly();
        }
    }
}
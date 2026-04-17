package src.model;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SessionStore {
    private final Path usersFile = Paths.get("data", "users.txt");

    public String getLoggedInUsername() {
        try (BufferedReader reader = Files.newBufferedReader(usersFile)) {
            String line = reader.readLine();
            if (line != null) {
                return line.split(":")[0].trim();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read logged-in user", e);
        }
        return null;
    }
}

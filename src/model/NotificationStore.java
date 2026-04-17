package src.model;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationStore {
	private final Path notificationsFile = Paths.get("data", "notifications.txt");

	public List<Notification> findNotificationsFor(String username) {
		List<Notification> result = new ArrayList<>();

		try (BufferedReader reader = Files.newBufferedReader(notificationsFile)) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(";");

				if (parts.length >= 4 && parts[0].trim().equals(username)) {
					result.add(
							new Notification(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not read notifications", e);
		}

		return result;
	}

	public void addNotification(String recipientUsername, String actorUsername, String imageId) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		String line = recipientUsername + ";" + actorUsername + ";" + imageId + ";" + timestamp;

		try (BufferedWriter writer = Files.newBufferedWriter(notificationsFile, StandardOpenOption.CREATE,
				StandardOpenOption.APPEND)) {
			writer.write(line);
			writer.newLine();
		} catch (IOException e) {
			throw new RuntimeException("Could not write notification", e);
		}
	}
}

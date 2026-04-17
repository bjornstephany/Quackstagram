package src.model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostStore {
    private final Path uploadedDir = Paths.get("img", "uploaded");
    private final Path imageDetailsFile = Paths.get("img", "image_details.txt");

    public void saveUploadedPost(String username, File selectedFile, String caption) {
        int imageId = getNextImageId(username);
        String extension = getFileExtension(selectedFile);
        String newFileName = username + "_" + imageId + "." + extension;

        try {
            Path destPath = uploadedDir.resolve(newFileName);
            Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

            saveImageInfo(username + "_" + imageId, username, caption);
        } catch (IOException e) {
            throw new RuntimeException("Could not save uploaded post", e);
        }
    }

    private int getNextImageId(String username) {
        int maxId = 0;

        if (!Files.exists(imageDetailsFile)) {
            return 1;
        }

        try (BufferedReader reader = Files.newBufferedReader(imageDetailsFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Username: " + username)) {
                    String[] parts = line.split(", ");
                    String imageIdPart = parts[0].split(": ")[1];
                    String[] idTokens = imageIdPart.split("_");
                    int id = Integer.parseInt(idTokens[1]);
                    maxId = Math.max(maxId, id);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not determine next image id", e);
        }

        return maxId + 1;
    }

    private void saveImageInfo(String imageId, String username, String bio) {
        try {
            if (!Files.exists(imageDetailsFile)) {
                Files.createFile(imageDetailsFile);
            }

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            try (BufferedWriter writer = Files.newBufferedWriter(
                    imageDetailsFile,
                    StandardOpenOption.APPEND)) {
                writer.write(String.format(
                        "ImageID: %s, Username: %s, Bio: %s, Timestamp: %s, Likes: 0",
                        imageId, username, bio, timestamp));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not write image metadata", e);
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "png";
    }
}
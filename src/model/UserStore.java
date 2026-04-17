package src.model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserStore {
    private final Path credentialsFile = Paths.get("data", "credentials.txt");
    private final Path followingFile = Paths.get("data", "following.txt");
    private final Path imageDetailsFile = Paths.get("img", "image_details.txt");

    private final SessionStore sessionStore = new SessionStore();

    public ProfileData loadProfile(String viewedUsername) {
        String loggedInUsername = sessionStore.getLoggedInUsername();

        int postCount = countPosts(viewedUsername);
        int followerCount = countFollowers(viewedUsername);
        int followingCount = countFollowing(viewedUsername);
        String bio = findBio(viewedUsername);

        boolean isCurrentUser = viewedUsername.equals(loggedInUsername);
        boolean followedByLoggedInUser =
                loggedInUsername != null
                && !isCurrentUser
                && isFollowing(loggedInUsername, viewedUsername);

        return new ProfileData(
                viewedUsername,
                bio,
                postCount,
                followerCount,
                followingCount,
                isCurrentUser,
                followedByLoggedInUser
        );
    }

    private int countPosts(String username) {
        int count = 0;

        if (!Files.exists(imageDetailsFile)) {
            return 0;
        }

        try (BufferedReader reader = Files.newBufferedReader(imageDetailsFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Username: " + username)) {
                    count++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not count posts", e);
        }

        return count;
    }

    private int countFollowers(String username) {
        int followers = 0;

        if (!Files.exists(followingFile)) {
            return 0;
        }

        try (BufferedReader reader = Files.newBufferedReader(followingFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String[] followedUsers = parts[1].split(";");
                    for (String followedUser : followedUsers) {
                        if (followedUser.trim().equals(username)) {
                            followers++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not count followers", e);
        }

        return followers;
    }

    private int countFollowing(String username) {
        if (!Files.exists(followingFile)) {
            return 0;
        }

        try (BufferedReader reader = Files.newBufferedReader(followingFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].trim().equals(username)) {
                    if (parts[1].trim().isEmpty()) {
                        return 0;
                    }
                    return parts[1].split(";").length;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not count following", e);
        }

        return 0;
    }

    private String findBio(String username) {
        if (!Files.exists(credentialsFile)) {
            return "";
        }

        try (BufferedReader reader = Files.newBufferedReader(credentialsFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    return parts[2];
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load bio", e);
        }

        return "";
    }

    public boolean isFollowing(String followerUsername, String followedUsername) {
        if (!Files.exists(followingFile)) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(followingFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].trim().equals(followerUsername)) {
                    String[] followedUsers = parts[1].split(";");
                    for (String followedUser : followedUsers) {
                        if (followedUser.trim().equals(followedUsername)) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not check following status", e);
        }

        return false;
    }
    
    public void followUser(String followerUsername, String followedUsername) {
        Path followingFilePath = Paths.get("data", "following.txt");

        if (followerUsername == null || followerUsername.isBlank()) {
            return;
        }

        try {
            boolean found = false;
            StringBuilder newContent = new StringBuilder();

            if (Files.exists(followingFilePath)) {
                try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        if (parts[0].trim().equals(followerUsername)) {
                            found = true;

                            boolean alreadyFollowing = false;
                            if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                                String[] followedUsers = parts[1].split(";");
                                for (String followedUser : followedUsers) {
                                    if (followedUser.trim().equals(followedUsername)) {
                                        alreadyFollowing = true;
                                        break;
                                    }
                                }
                            }

                            if (!alreadyFollowing) {
                                line = line.concat(line.endsWith(":") ? "" : "; ").concat(followedUsername);
                            }
                        }
                        newContent.append(line).append("\n");
                    }
                }
            }

            if (!found) {
                newContent.append(followerUsername).append(": ").append(followedUsername).append("\n");
            }

            try (BufferedWriter writer = Files.newBufferedWriter(followingFilePath)) {
                writer.write(newContent.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not update following relationship", e);
        }
    }
}
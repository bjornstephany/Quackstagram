package src.model;
public class Notification {
    private final String recipientUsername;
    private final String actorUsername;
    private final String imageId;
    private final String timestamp;

    public Notification(String recipientUsername, String actorUsername, String imageId, String timestamp) {
        this.recipientUsername = recipientUsername;
        this.actorUsername = actorUsername;
        this.imageId = imageId;
        this.timestamp = timestamp;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public String getActorUsername() {
        return actorUsername;
    }

    public String getImageId() {
        return imageId;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

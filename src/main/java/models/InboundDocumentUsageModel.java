package models;

import org.springframework.data.annotation.Id;
import org.springframework.data.util.StreamUtils;

public class InboundDocumentUsageModel {

    @Id
    private String id;

    private String resId;

    private String userId;

    private String authorId;

    private int timeQuota;

    public InboundDocumentUsageModel() {}

    public InboundDocumentUsageModel(String resId, String userId, int timeQuota) {
        this.resId = resId;
        this.userId = userId;
        this.timeQuota = timeQuota;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setTimeQuota(int timeQuota) {
        this.timeQuota = timeQuota;
    }

    public int getTimeQuota() {
        return timeQuota;
    }

    @Override
    public String toString() {
        return String.format("InboundDocumentUsageModel id: %s resId: %s authorId: %s userId: %s timeQuota: %d",
                id, resId, authorId, userId, timeQuota);
    }

}

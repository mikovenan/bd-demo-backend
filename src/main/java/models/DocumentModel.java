package models;

import org.springframework.data.annotation.Id;

public class DocumentModel implements ExchangeModel {

    @Id
    private String id;

    private String url;

    private String ownerId;

    public DocumentModel() {}

    public DocumentModel(String url) {
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void SetOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public String toString() {
        return String.format("DocumentModel: id: %s url: %s ownerId: %s", id, url, ownerId);
    }

    @Override
    public boolean isValid() {
        // Ideally we should also check if URL is valid but that's a bit overkill for a saturday night.
        return url != null && url.length() > 0;
    }
}

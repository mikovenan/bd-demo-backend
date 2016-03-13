package models;

import org.springframework.data.annotation.Id;

public class InboundDocumentModel {

    @Id
    private String id;

    private String url;

    private String ownerId;

    public InboundDocumentModel() {}

    public InboundDocumentModel(String url) {
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
        return String.format("InboundDocumentModel: id: %s url: %s ownerId: %s", id, url, ownerId);
    }
}

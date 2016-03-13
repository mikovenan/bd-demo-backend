package models;

public class ExchangeDocumentModel implements ExchangeModel {

    private String id;

    private String url;

    private String ownerEmail;

    public ExchangeDocumentModel() {}

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("ExchangeDocumentModel: id: %s owner: %s url: %s", id, ownerEmail, url);
    }

    @Override
    public boolean isValid() {
        // Ideally we should also check if URL is valid but that's a bit overkill for a saturday night.
        return url != null && url.length() > 0;
    }
}

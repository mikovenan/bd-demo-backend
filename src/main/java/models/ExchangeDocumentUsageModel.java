package models;

import org.springframework.data.annotation.Id;

public class ExchangeDocumentUsageModel implements ExchangeModel {

    private String docId;

    private int quota;

    private String url;

    private String authorEmail;

    private String userEmail;

    public ExchangeDocumentUsageModel() {}

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Override
    public boolean isValid() {
        return quota > 0;
    }
}

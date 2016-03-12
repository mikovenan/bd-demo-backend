package models;

public class ExchangeDocumentUsageModel implements ExchangeModel {

    private int quota;

    public ExchangeDocumentUsageModel() {}

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    @Override
    public boolean isValid() {
        return quota > 0;
    }
}

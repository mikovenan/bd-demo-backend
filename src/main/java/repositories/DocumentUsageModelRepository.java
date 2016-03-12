package repositories;

import models.InboundDocumentUsageModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentUsageModelRepository extends MongoRepository<InboundDocumentUsageModel, String> {
    public List<InboundDocumentUsageModel> findByAuthorId(String authorId);
    public List<InboundDocumentUsageModel> findByResId(String resId);
}

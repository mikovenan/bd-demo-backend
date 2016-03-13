package repositories;

import models.InboundDocumentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentModelRepository extends MongoRepository<InboundDocumentModel, String> {

}

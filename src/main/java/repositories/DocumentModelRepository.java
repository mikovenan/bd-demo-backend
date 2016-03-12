package repositories;

import models.DocumentModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentModelRepository extends MongoRepository<DocumentModel, String> {

}

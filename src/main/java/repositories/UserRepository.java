package repositories;

import models.InboundUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import security.SecurityService;

public interface UserRepository extends MongoRepository<InboundUserModel, String> {
    public InboundUserModel findByEmail(String email);

    default public InboundUserModel getUserFromToken(String token) {
        if (token == null)
            return null;

        String userEmail = SecurityService.get().getSubjectFromJWTToken(token);
        if (userEmail == null)
            return null;

        return findByEmail(userEmail);
    }
}

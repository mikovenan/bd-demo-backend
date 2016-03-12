package api;

import models.DocumentModel;
import models.ExchangeDocumentUsageModel;
import models.InboundDocumentUsageModel;
import models.InboundUserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.DocumentModelRepository;
import repositories.DocumentUsageModelRepository;
import repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentAPIImpl implements DocumentAPI {

    static Logger logger = Logger.getLogger(DocumentAPIImpl.class.getName());

    @Autowired
    UserRepository userRepository;

    @Autowired
    DocumentModelRepository documentModelRepository;

    @Autowired
    DocumentUsageModelRepository documentUsageModelRepository;

    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createDocument(@RequestBody DocumentModel documentModel,
                                                 @RequestHeader("Authorization") String auth) {
        if (!documentModel.isValid())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        InboundUserModel userModel = userRepository.getUserFromToken(auth);
        if (userModel == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        logger.info("User: " + userModel.getId() + " creating document: " + documentModel.toString());

        documentModel.SetOwnerId(userModel.getId());

        documentModel = documentModelRepository.save(documentModel);

        return new ResponseEntity<>(documentModel.getId(), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/{doc_id}/usage", method = RequestMethod.POST)
    public ResponseEntity<String> sendUsageForResource(@PathVariable(value = "doc_id") String id,
           @RequestBody ExchangeDocumentUsageModel exchangeDocumentUsageModel,
           @RequestHeader("Authorization") String auth) {

        InboundUserModel userModel = userRepository.getUserFromToken(auth);
        if (userModel == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        logger.info("User: " + userModel.getId() + " sending usage for resource id: " + id + " with quota: " +
            exchangeDocumentUsageModel.getQuota());

        DocumentModel documentModel = documentModelRepository.findOne(id);
        if (documentModel == null || !exchangeDocumentUsageModel.isValid())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<InboundDocumentUsageModel> inboundDocumentUsageModels = documentUsageModelRepository.findByResId(id);
        if (inboundDocumentUsageModels == null || inboundDocumentUsageModels.isEmpty()) {
            InboundDocumentUsageModel documentUsageModel = new InboundDocumentUsageModel();
            documentUsageModel.setResId(id);
            documentUsageModel.setUserId(userModel.getId());
            documentUsageModel.setTimeQuota(exchangeDocumentUsageModel.getQuota());
            documentUsageModel.setAuthorId(documentModel.getOwnerId());

            documentUsageModelRepository.save(documentUsageModel);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // FIXME: Use a proper mongo driver and do a proper query.
        for (InboundDocumentUsageModel doc : inboundDocumentUsageModels) {
            if (doc.getUserId().equals(userModel.getId())) {
                doc.setTimeQuota(doc.getTimeQuota() + exchangeDocumentUsageModel.getQuota());

                documentUsageModelRepository.save(doc);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    @RequestMapping(value = "/usage", method = RequestMethod.GET)
    public ResponseEntity<List<InboundDocumentUsageModel>> getUsageForResources(
        @RequestHeader("Authorization") String auth) {

        InboundUserModel userModel = userRepository.getUserFromToken(auth);
        if (userModel == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        logger.info("User: " + userModel.getId() + " getting usage for all resources");

        List<InboundDocumentUsageModel> documentUsageModels =
            documentUsageModelRepository.findByAuthorId(userModel.getId());

        return new ResponseEntity<>(documentUsageModels, HttpStatus.OK);
    }
}

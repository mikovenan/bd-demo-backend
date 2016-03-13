package api;

import models.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.DocumentModelRepository;
import repositories.DocumentUsageModelRepository;
import repositories.UserRepository;

import java.util.ArrayList;
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
    public ResponseEntity<ExchangeDocumentModel> createDocument(@RequestBody ExchangeDocumentModel exchangeDocumentModel,
                                                                @RequestHeader("Authorization") String auth) {
        if (!exchangeDocumentModel.isValid())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        InboundUserModel userModel = userRepository.getUserFromToken(auth);
        if (userModel == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        InboundDocumentModel inboundDocumentModel = new InboundDocumentModel();
        inboundDocumentModel.setUrl(exchangeDocumentModel.getUrl());
        inboundDocumentModel.SetOwnerId(userModel.getId());

        documentModelRepository.save(inboundDocumentModel);

        exchangeDocumentModel.setOwnerEmail(userModel.getEmail());
        exchangeDocumentModel.setId(inboundDocumentModel.getId());

        logger.info("User: " + userModel.getId() + " creating document: " + exchangeDocumentModel.toString());

        return new ResponseEntity<>(exchangeDocumentModel, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/{doc_id}/preview", method = RequestMethod.GET)
    public ResponseEntity<ExchangeDocumentModel> getDocument(@PathVariable(value="doc_id") String id,
                                                            @RequestHeader("Authorization") String auth) {
        InboundUserModel userModel = userRepository.getUserFromToken(auth);
        if (userModel == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        InboundDocumentModel inboundDocumentModel = documentModelRepository.findOne(id);

        if (inboundDocumentModel == null) {
            logger.info("User: " + userModel.getId() + " tried to fetch invalid doc id: " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        InboundUserModel docOwner = userRepository.findOne(inboundDocumentModel.getOwnerId());

        logger.info("User: " + userModel.getId() + " fetching document: " + inboundDocumentModel.toString());

        ExchangeDocumentModel exchangeDocumentModel = new ExchangeDocumentModel();
        exchangeDocumentModel.setUrl(inboundDocumentModel.getUrl());
        exchangeDocumentModel.setOwnerEmail(docOwner.getEmail());
        exchangeDocumentModel.setId(id);

        return new ResponseEntity<>(exchangeDocumentModel, HttpStatus.OK);

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

        InboundDocumentModel inboundDocumentModel = documentModelRepository.findOne(id);
        if (inboundDocumentModel == null || !exchangeDocumentUsageModel.isValid())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<InboundDocumentUsageModel> inboundDocumentUsageModels = documentUsageModelRepository.findByResId(id);

        // FIXME: Use a proper mongo driver and do a proper query.
        for (InboundDocumentUsageModel doc : inboundDocumentUsageModels) {
            if (doc.getUserId().equals(userModel.getId())) {
                doc.setTimeQuota(doc.getTimeQuota() + exchangeDocumentUsageModel.getQuota());

                documentUsageModelRepository.save(doc);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        // There is no such resource, we will create one.
        InboundDocumentUsageModel documentUsageModel = new InboundDocumentUsageModel();
        documentUsageModel.setResId(id);
        documentUsageModel.setUserId(userModel.getId());
        documentUsageModel.setTimeQuota(exchangeDocumentUsageModel.getQuota());
        documentUsageModel.setAuthorId(inboundDocumentModel.getOwnerId());

        documentUsageModelRepository.save(documentUsageModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/usage", method = RequestMethod.GET)
    public ResponseEntity<List<ExchangeDocumentUsageModel>> getUsageForResources(
        @RequestHeader("Authorization") String auth) {

        InboundUserModel userModel = userRepository.getUserFromToken(auth);
        if (userModel == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        logger.info("User: " + userModel.getId() + " getting usage for all resources");

        List<InboundDocumentUsageModel> documentUsageModels =
            documentUsageModelRepository.findByAuthorId(userModel.getId());


        List<ExchangeDocumentUsageModel> outboundUsageModels = new ArrayList<>();
        // This is really heavy, a cache should be in place.
        for (InboundDocumentUsageModel document : documentUsageModels) {
            ExchangeDocumentUsageModel exchangeDocumentUsageModel = new ExchangeDocumentUsageModel();
            exchangeDocumentUsageModel.setQuota(document.getTimeQuota());

            InboundUserModel user = userRepository.findOne(document.getUserId());
            InboundDocumentModel doc = documentModelRepository.findOne(document.getResId());

            exchangeDocumentUsageModel.setAuthorEmail(userModel.getEmail());
            exchangeDocumentUsageModel.setUserEmail(user.getEmail());
            exchangeDocumentUsageModel.setUrl(doc.getUrl());
            exchangeDocumentUsageModel.setDocId(doc.getId());

            outboundUsageModels.add(exchangeDocumentUsageModel);
        }

        return new ResponseEntity<>(outboundUsageModels, HttpStatus.OK);
    }
}

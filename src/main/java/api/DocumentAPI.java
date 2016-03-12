package api;

import models.DocumentModel;
import models.ExchangeDocumentUsageModel;
import models.InboundDocumentUsageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public interface DocumentAPI {

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createDocument(@RequestBody DocumentModel documentModel,
                                                 @RequestHeader("Authorization") String auth);

    @RequestMapping(value = "/{doc_id}/usage", method = RequestMethod.POST)
    public ResponseEntity<String> sendUsageForResource(@PathVariable(value="someID") String id,
         @RequestBody ExchangeDocumentUsageModel exchangeDocumentUsageModel,
         @RequestHeader("Authorization") String auth);

    @RequestMapping(value = "/usage", method = RequestMethod.GET)
    public ResponseEntity<List<InboundDocumentUsageModel>> getUsageForResources(
        @RequestHeader("Authorization") String auth);
}

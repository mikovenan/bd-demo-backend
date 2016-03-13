package api;

import models.ExchangeDocumentModel;
import models.ExchangeDocumentUsageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public interface DocumentAPI {

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ExchangeDocumentModel> createDocument(@RequestBody ExchangeDocumentModel inboundDocumentModel,
                                                                @RequestHeader("Authorization") String auth);

    @RequestMapping(value = "/{doc_id}/preview", method = RequestMethod.GET)
    public ResponseEntity<ExchangeDocumentModel> getDocument(@PathVariable(value="doc_id") String id,
                                                            @RequestHeader("Authorization") String auth);


    @RequestMapping(value = "/{doc_id}/usage", method = RequestMethod.POST)
    public ResponseEntity<String> sendUsageForResource(@PathVariable(value="doc_id") String id,
         @RequestBody ExchangeDocumentUsageModel exchangeDocumentUsageModel,
         @RequestHeader("Authorization") String auth);

    @RequestMapping(value = "/usage", method = RequestMethod.GET)
    public ResponseEntity<List<ExchangeDocumentUsageModel>> getUsageForResources(
        @RequestHeader("Authorization") String auth);
}

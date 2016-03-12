package api;

import models.ExchangeUserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public interface UserAPI {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody ExchangeUserModel exchangeUserModel);

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<String> signup(@RequestBody ExchangeUserModel exchangeUserModel);
}

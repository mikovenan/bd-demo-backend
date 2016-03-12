package api;

import models.ExchangeUserModel;
import models.InboundUserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import repositories.UserRepository;
import security.SecurityService;

@RestController
@RequestMapping("/auth")
public class UserAPIImpl implements UserAPI {

    static Logger logger = Logger.getLogger(UserAPIImpl.class.getName());

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody ExchangeUserModel exchangeUserModel) {

        if (!exchangeUserModel.isValid())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        logger.info("Login attempt for: " + exchangeUserModel.toString());

        InboundUserModel existingUser = userRepository.findByEmail(exchangeUserModel.getEmail());

        if (existingUser == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!existingUser.isPasswordValid(exchangeUserModel.getPassword()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String token = SecurityService.get().getJWTForSubject(exchangeUserModel.getEmail());

        if (token != null) {
            logger.info("Login success for: " + exchangeUserModel.toString());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<String> signup(@RequestBody ExchangeUserModel exchangeUserModel) {

        if (!exchangeUserModel.isValid())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        logger.info("Signup attempt for: " + exchangeUserModel.toString());

        InboundUserModel existingUser = userRepository.findByEmail(exchangeUserModel.getEmail());

        if (existingUser != null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        InboundUserModel user = new InboundUserModel(exchangeUserModel.getEmail(), exchangeUserModel.getPassword());
        userRepository.save(user);

        String token = SecurityService.get().getJWTForSubject(exchangeUserModel.getEmail());

        if (token != null) {
            logger.info("Signup success for: " + exchangeUserModel.toString());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

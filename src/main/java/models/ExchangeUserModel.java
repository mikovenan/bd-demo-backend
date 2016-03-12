package models;

import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.validator.routines.EmailValidator;

public class ExchangeUserModel implements ExchangeModel {

    private static final int MIN_PASS_LENGTH = 6;

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ExchangeUserModel() {}

    public ExchangeUserModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean isValid() {
        return this.email != null && this.email.length() != 0 && EmailValidator.getInstance().isValid(email)
                && this.password != null && this.password.length() >= MIN_PASS_LENGTH;
    }

    @Override
    public String toString() {
        return String.format("User: email: %s", email);
    }
}

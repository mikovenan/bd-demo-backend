package models;

import org.springframework.data.annotation.Id;
import security.SecurityService;

public class InboundUserModel {

    @Id
    private String id;

    private String email;

    private String salt;

    private String hash;

    public InboundUserModel() {}

    public InboundUserModel(String email, String salt, String hash) {
        this.email = email;
        this.salt = salt;
        this.hash = hash;
    }

    public InboundUserModel(String email, String password) {
        this.email = email;
        this.salt = SecurityService.get().generateNewSalt();
        this.hash = SecurityService.get().generateNewHashFromPasswordAndSalt(password, salt);
    }

    public boolean isPasswordValid(String password) {
        return SecurityService.get().isHashValidForPasswordWithSalt(hash, password, salt);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return String.format("InboundUserModel: id: %s email: %s", id, email);
    }
}

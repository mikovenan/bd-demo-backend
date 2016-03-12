package security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecurityService {

    static Logger logger = Logger.getLogger(SecurityService.class.getName());

    private static SecurityService instance = null;

    private SecureRandom secureRandom;

    private static final int SALT_SIZE = 16;

    private String jwtSalt;

    private SecurityService() throws NoSuchAlgorithmException {
        secureRandom = SecureRandom.getInstance("SHA1PRNG");

        jwtSalt = System.getenv().get("JWT_SALT");

        if (jwtSalt == null) {
            logger.error("JWT_SALT not set in environment variables. Exiting");
            System.exit(-1);
        }
    }

    public static SecurityService get() {
        if(instance == null) {
            try {
                instance = new SecurityService();
            } catch (NoSuchAlgorithmException e) {
                logger.error("Missing Encryption algorithm, check your env setup, this is very embarrassing");
                System.exit(-1);
            }
        }

        return instance;
    }

    public boolean isHashValidForPasswordWithSalt(String hash, String password, String salt) {
        String hashForPasswordAndSalt = generateNewHashFromPasswordAndSalt(password, salt);

        return hashForPasswordAndSalt.equals(hash);
    }

    public String generateNewSalt() {
        byte[] salt = new byte[SALT_SIZE];
        secureRandom.nextBytes(salt);

        return new String(salt);
    }

    public String generateNewHashFromPasswordAndSalt(String password, String salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt.getBytes());

            byte[] digest = messageDigest.digest(password.getBytes());
            return new String(digest);
        } catch (NoSuchAlgorithmException exception) {
            logger.error("Missing Encryption algorithm, check your env setup, this is very embarrassing");
            System.exit(-1);
        }

        return null;
    }

    public String getJWTForSubject(String subject) {
        try {
            return Jwts.builder().setSubject(subject).signWith(SignatureAlgorithm.HS512, jwtSalt).compact();
        } catch (io.jsonwebtoken.MalformedJwtException exception) {
            logger.info("Attempted to decode an invalid jwt token. Bailing");
        }

        return null;
    }

    public String getSubjectFromJWTToken(String token) {
        if (token == null)
            return null;

        return Jwts.parser().setSigningKey(jwtSalt).parseClaimsJws(token).getBody().getSubject();
    }
}

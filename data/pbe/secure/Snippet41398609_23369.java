package entities;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Andrew
 * A class controlling the logic relating to the hashing of passwords.
 * 
 */
public class PasswordManager {
    String ptPassword;//Plain text 
    String hashedPassword;//Hashed
    byte[] salt;

    public PasswordManager(){
        this.ptPassword = null;
        this.hashedPassword = null;
        this.salt = null;
    }

    //Generates a salt for hashing a password
    /**
     * Uses SecureRandom and the SHA-1 Pseudo Random Number Generator to create a 
     * salt that is extremely difficult to predict.
     * Possibly a bit overkill for the security on a simple travel application, however
     * this method is good for scalability were we looking to up our game.
     * 
     * @return 
     * @throws java.security.NoSuchAlgorithmException
     */
    public byte[] generateSalt() throws NoSuchAlgorithmException{
        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
        byte[] randomBytes = new byte[128];
        secureRandomGenerator.nextBytes(randomBytes);

        return randomBytes;
    }

    //Uses the plaintext password and the salt to generate a hashed password to store in the database
    /** 
     * 
     * @param password
     * @param salt
     * @return
     * @throws Exception 
     */
    public String generateHash(String password, byte[] salt) throws Exception{
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(password.toCharArray(), salt, 2000, 128));

        String encoded = DatatypeConverter.printBase64Binary(key.getEncoded());
        byte[] decoded = DatatypeConverter.parseBase64Binary(encoded);

        String output = new String(decoded, "UTF-8");

        return output;
    } 


    //Helper method for conversion of salt to a database storable object
    /**
     * 
     * @param salt
     * @return 
     */
    public String saltToString(byte[] salt){
        return salt.toString();
    }

    //Helper method for conversion of salt to a hashable object
    /**
     * 
     * @param salt
     * @return 
     */
    public byte[] saltToBytes(String salt){
        return salt.getBytes();
    }

    public String getPtPassword() {
        return ptPassword;
    }

    public void setPtPassword(String ptPassword) {
        this.ptPassword = ptPassword;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }


}

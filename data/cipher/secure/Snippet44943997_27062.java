import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class EfiAdminPasswordHasher {
    /** SIZE variable.*/
    private static final int SIZE = 16;
    /** The serial number. */
    private static final long serialVersionUID = -7930024972049607076L;
    private static final String SECRET_KEY = "Dp29eInG$fch@ujO";
    private static final String SALT = "I@j2ts_@p$c29qTw";
    private SecretKey secretKey;


    public EfiAdminPasswordHasher() {
        generateSecretKey();
    }

    private void generateSecretKey() {
        try {

            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(),
                    SALT.getBytes(), 65536, 256);
            SecretKey keySpec = factory.generateSecret(spec);
            final SecretKey secret = new SecretKeySpec(keySpec.getEncoded(),
                    "AES");
            System.out.println(secret+" "+"In SECRET kEY");
            secretKey = secret;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(
                    "EfiAdminPasswordHasher::decryptPassword() - Unable to decrypt password.",
                    e);
        }
    }


public String encryptPassword(String password) {
        if (password == null || password.trim().length() == 0) {
            return password;
        }
        // Encoding password with SHA-256 algorithm
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            byte byteData[] = md.digest();
            //convert the byte to hex format method 1
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
             sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException | IllegalStateException e) {
            throw new IllegalStateException(
                    "EfiAdminPasswordHasher::encryptPassword() - Unable to encrypt password.",
                    e);
        }
    }


public String decryptPassword(String encryptedPassword) {

     // Cannot use StringUtils here, because this class to be serialized and
        // sent over to ACC
        if (encryptedPassword == null || encryptedPassword.trim().length() == 0) {
            return encryptedPassword;
        }
        // DECODE encryptedPwd String
        try {
            final byte[] encrypedPwdBytes = Base64.decodeBase64(encryptedPassword.getBytes());
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] iv = new byte[SIZE];
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            final byte[] plainTextPwdBytes = cipher.doFinal(encrypedPwdBytes);

            return new String(plainTextPwdBytes);
        } catch (IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException |
                        InvalidKeyException | IllegalStateException | BadPaddingException |
                        InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(
                    "EfiAdminPasswordHasher::decryptPassword() - Unable to decrypt password.",
                    e);
        }
    }


public static void main(String[] args) {
        EfiAdminPasswordHasher hasher = new EfiAdminPasswordHasher();
        String encryptedPassword=hasher.encryptPassword("2");
        System.out.println("Encrypted password"+" "+encryptedPassword);


        String decryptedPassword=hasher.decryptPassword(encryptedPassword);
        System.out.println("Decrypted password"+" "+decryptedPassword);

    }
}

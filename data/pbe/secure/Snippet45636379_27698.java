 <Resource name="jdbc/HasanDB" auth="Container" type="javax.sql.DataSource"
              maxTotal="10" maxIdle="15" minIdle="3" initialSize="2" maxWaitMillis="10000"
              removeAbondend="true" removeAbondendTimeout="300"
              username="hasan" password="<encryptedpass>" driverClassName="com.mysql.jdbc.Driver"
              url="jdbc:mysql://localhost:3306/HasanDB"/>

package hasan;
public class Http11Nio2Protocol extends org.apache.coyote.http11.Http11Nio2Protocol  {
    @Override
    public void setKeystorePass(String certificateKeystorePassword) {
        try {
            System.out.println("..............===============certificateKeystorePassword===========................");
            super.setKeystorePass(EncryptService.decrypt(certificateKeystorePassword));
        } catch (final Exception e){
            super.setKeystorePass("");
        }
    }
}

package hasan;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptService {
    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }
    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
    static String decrypt(String string) throws GeneralSecurityException, IOException {
        String password = "password";
        byte[] salt = new String("salt").getBytes();
        int iterationCount = 100;
        int keyLength = 128;
        SecretKeySpec key = createSecretKey(password.toCharArray(),
                salt, iterationCount, keyLength);
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }
}

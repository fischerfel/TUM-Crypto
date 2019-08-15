import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;

public class Main {
    public static void main(String[] argv) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        FileInputStream is = new FileInputStream("wso2carbon.jks");

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, "wso2carbon".toCharArray());

        String alias = "wso2carbon";

        Key key = keystore.getKey(alias, "wso2carbon".toCharArray());
        if (key instanceof PrivateKey) {
            Certificate cert = keystore.getCertificate(alias);

            PublicKey publicKey = cert.getPublicKey();

            String dataToBeEncrypted = "admin";
            String adminToDecrypted = "kuv2MubUUveMyv6GeHrXr9il59ajJIqUI4eoYHcgGKf/BBFOWn96NTjJQI+wYbWjKW6r79S7L7ZzgYeWx7DlGbff5X3pBN2Gh9yV0BHP1E93QtFqR7uTWi141Tr7V7ZwScwNqJbiNoV+vyLbsqKJE7T3nP8Ih9Y6omygbcLcHzg=";

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String encryptedData = Base64.encodeBase64String(cipher.doFinal(dataToBeEncrypted.getBytes()));
            System.out.println("Encrypted Data: " + encryptedData);

            Cipher dipher = Cipher.getInstance("RSA");

            dipher.init(Cipher.DECRYPT_MODE, key);
            System.out.println(new String(dipher.doFinal(Base64.decodeBase64(encryptedData))));

        }
    }
}

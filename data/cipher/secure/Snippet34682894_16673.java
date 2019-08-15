import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class Decrypter {
String keyFileName;

public Decrypter(String keyFileName) {
    this.keyFileName = keyFileName;
}

public String rsaDecrypt(String str) throws Exception {
    PrivateKey privateKey = readPrivateKeyFromFile(keyFileName);
    Cipher cipher = Cipher.getInstance("RSA");

    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] dec = Base64.decodeBase64(str);
    byte[] utf8 = cipher.doFinal(Base64.decodeBase64(dec));
    return Base64.encodeBase64String(utf8);

}

private PrivateKey readPrivateKeyFromFile(String keyFileName) throws Exception {
    File filePrivateKey = new File(keyFileName);
    FileInputStream fis = new FileInputStream(keyFileName);
    byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
    fis.read(encodedPrivateKey);
    fis.close();

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
            encodedPrivateKey);
    PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
    return privateKey;
}
}

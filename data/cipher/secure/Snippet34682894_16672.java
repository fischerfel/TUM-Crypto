import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class Encrypter {
String keyFileName;

public Encrypter(String keyFileName) {
    this.keyFileName = keyFileName;
}

public String rsaEncrypt(String data) throws Exception {
    PublicKey pubKey = readPublicKeyFromFile(keyFileName);
            byte[] utf8 = data.getBytes("UTF-8");
    Cipher cipher = Cipher.getInstance("RSA");

    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    byte[] enc = cipher.doFinal(Base64.encodeBase64(utf8));
    return Base64.encodeBase64String(enc);
}

private PublicKey readPublicKeyFromFile(String keyFileName) throws Exception {
    File filePublicKey = new File(keyFileName);
    FileInputStream fis = new FileInputStream(keyFileName);
    byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
    fis.read(encodedPublicKey);
    fis.close();

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
            encodedPublicKey);
    PublicKey pubKey = keyFactory.generatePublic(publicKeySpec);
    return pubKey;
}
}

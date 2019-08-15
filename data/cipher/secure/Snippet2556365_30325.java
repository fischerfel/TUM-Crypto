import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class AsyncronousKeyTest {

    private final Cipher cipher;
    private final KeyFactory keyFactory;
    private final RSAPrivateKey privKey;

    private AsyncronousKeyTest() throws Exception {
 cipher = Cipher.getInstance("AES/CBC/NoPaddin", "BC");
 keyFactory = KeyFactory.getInstance("AES", "BC");

 // create the keys

 RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(new BigInteger(
  "d46f473a2d746537de2056ae3092c451", 16), new BigInteger("57791d5430d593164082036ad8b29fb1",
  16));
 privKey = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);

    }

    public void generateAuthorizationAct(OutputStream outputStream) throws Exception {

 KeyFactory keyFactory = KeyFactory.getInstance("AES", "BC");
 RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger("d46f473a2d746537de2056ae3092c451",
  16), new BigInteger("11", 16));
 RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

 byte[] data = new byte[] {0x01};

 byte[] encrypted = encryptAO(pubKey, data);
 outputStream.write(encrypted);
    }

    /** Encrypt the AuthorizationObject. */
    public byte[] encryptAO(Key pubKey, byte[] data) throws Exception {
 cipher.init(Cipher.ENCRYPT_MODE, pubKey);
 byte[] cipherText = cipher.doFinal(data);
 return cipherText;
    }

    public byte[] decrypt(byte[] cipherText) throws Exception {
 cipher.init(Cipher.DECRYPT_MODE, privKey);
 byte[] decyptedData = cipher.doFinal(cipherText);
 return decyptedData;

    }

    public static void main(String[] args) throws Exception {
 System.out.println("start");

 AsyncronousKeyTest auth = new AsyncronousKeyTest();
 auth.generateAuthorizationAct(System.out);

 System.out.println("done");
    }

}

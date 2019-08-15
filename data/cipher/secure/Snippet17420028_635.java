import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Test {

static String PRIVATE_KEY;
static String PUBLIC_KEY;

public static void main(String args[]) throws Exception
{
    Test test = new Test();
    test.doSomething();

    String teststr = "wow this is a test string!";
    String resultstr = test.decrypt(test.encrypt(teststr));
    if (teststr.equals(resultstr)) {
        System.out.println("Equal");
        System.out.println(resultstr);
    }
}


public void doSomething() throws Exception{     
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    KeyPair keypair = kpg.genKeyPair();

    Key publicKey = keypair.getPublic();
    System.out.println("publicKey.getFormat():"+publicKey.getFormat());
    byte[] publicKeyBytes = publicKey.getEncoded();
    String base64PublicKeyString = Base64.encodeBase64String(publicKeyBytes);
    PUBLIC_KEY = base64PublicKeyString;
    System.out.println("Public Key");
    System.out.println(base64PublicKeyString);
    System.out.println("--------------------");

    Key privateKey = keypair.getPrivate();
    System.out.println("privateKey.getFormat():"+privateKey.getFormat());
    byte[] privateKeyBytes = privateKey.getEncoded();
    String base64PrivateKeyString = Base64.encodeBase64String(privateKeyBytes);
    PRIVATE_KEY = base64PrivateKeyString;
    System.out.println("Private Key");
    System.out.println(base64PrivateKeyString);
    System.out.println("--------------------");

}


private static final int DATA_BLOCK_SIZE = 117;

private static final int ENCRYPTED_BLOCK_SIZE = 128;

public static byte[] encrypt(String plaintext) throws Exception{
    Cipher cipher = Cipher.getInstance("RSA");
    byte[] privateKeyBytesFromString = Base64.decodeBase64(PRIVATE_KEY);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(privateKeyBytesFromString);
    PrivateKey pk = kf.generatePrivate(ks);

    cipher.init(Cipher.ENCRYPT_MODE, pk);
    byte[] bytes = plaintext.getBytes("UTF-8");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    for(int i = 0; i < (bytes.length/DATA_BLOCK_SIZE + 1); i++)
    {
        int start = i * DATA_BLOCK_SIZE;
        int blockLength;
        if(i == bytes.length/DATA_BLOCK_SIZE)
        {
            blockLength = bytes.length - i * DATA_BLOCK_SIZE;
        } else {
            blockLength = DATA_BLOCK_SIZE;
        }

        if(blockLength > 0)
        {
            byte[] encrypted = cipher.doFinal(bytes, start, blockLength);
            baos.write(encrypted);
        }
    }

    return baos.toByteArray();
}

public String decrypt(byte[] encrypted) throws Exception{
    Cipher cipher = Cipher.getInstance("RSA");

    byte[] publicKeyBytesFromString = Base64.decodeBase64(PUBLIC_KEY);
    //System.out.println(base64KeyString);
    KeyFactory kf = KeyFactory.getInstance("RSA");

    X509EncodedKeySpec ks = new X509EncodedKeySpec(publicKeyBytesFromString);
    PublicKey pk = kf.generatePublic(ks);


    cipher.init(Cipher.DECRYPT_MODE, pk);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    for(int i = 0; i < (encrypted.length/ENCRYPTED_BLOCK_SIZE); i++)
    {
        byte[] decrypted = cipher.doFinal(encrypted, i * ENCRYPTED_BLOCK_SIZE, ENCRYPTED_BLOCK_SIZE);
        baos.write(decrypted);
    }

    return new String(baos.toByteArray() ,"UTF-8");
}

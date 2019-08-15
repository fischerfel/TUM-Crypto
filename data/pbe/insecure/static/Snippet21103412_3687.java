import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

try
{
    String passEncrypt = "my password";
    byte[] saltEncrypt = "choose a better salt".getBytes();
    int iterationsEncrypt = 10000;
    SecretKeyFactory factoryKeyEncrypt = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    SecretKey tmp = factoryKeyEncrypt.generateSecret(new PBEKeySpec(
            passEncrypt.toCharArray(), saltEncrypt, iterationsEncrypt,
            128));
    SecretKeySpec encryptKey = new SecretKeySpec(tmp.getEncoded(),
            "AES");

    Cipher aesCipherEncrypt = Cipher
            .getInstance("AES/ECB/PKCS5Padding");
    aesCipherEncrypt.init(Cipher.ENCRYPT_MODE, encryptKey);

    // get the bytes
    byte[] bytes = StringUtils.getBytesUtf8(toEncodeEncryptString);

    // encrypt the bytes
    byte[] encryptBytes = aesCipherEncrypt.doFinal(bytes);

    // encode 64 the encrypted bytes
    String encoded = Base64.encodeBase64URLSafeString(encryptBytes);

    System.out.println("e: " + encoded);

    // assume some transport happens here

    // create a new string, to make sure we are not pointing to the same
    // string as the one above
    String encodedEncrypted = new String(encoded);

    //we recreate the same salt/encrypt as if its a separate system
    String passDecrypt = "my password";
    byte[] saltDecrypt = "choose a better salt".getBytes();
    int iterationsDecrypt = 10000;
    SecretKeyFactory factoryKeyDecrypt = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    SecretKey tmp2 = factoryKeyDecrypt.generateSecret(new PBEKeySpec(passDecrypt
            .toCharArray(), saltDecrypt, iterationsDecrypt, 128));
    SecretKeySpec decryptKey = new SecretKeySpec(tmp2.getEncoded(), "AES");

    Cipher aesCipherDecrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aesCipherDecrypt.init(Cipher.DECRYPT_MODE, decryptKey);

    //basically we reverse the process we did earlier

    // get the bytes from encodedEncrypted string
    byte[] e64bytes = StringUtils.getBytesUtf8(encodedEncrypted);

    // decode 64, now the bytes should be encrypted
    byte[] eBytes = Base64.decodeBase64(e64bytes);

    // decrypt the bytes
    byte[] cipherDecode = aesCipherDecrypt.doFinal(eBytes);

    // to string
    String decoded = StringUtils.newStringUtf8(cipherDecode);

    System.out.println("d: " + decoded);

}
catch (Exception e)
{
    e.printStackTrace();
}

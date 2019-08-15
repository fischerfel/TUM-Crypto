import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
          :

public class MyUtility {

    private static Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
    private static Cipher instance2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
    private static byte[] iv = { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };

    private static String encrypt(String data, DataParameters params) throws Exception {

        IvParameterSpec ivspec = new IvParameterSpec(iv);
        String privateKey = MyUtility.getKey(params);

        SecretKey key = new SecretKeySpec(privateKey.getBytes(), "AES");
        instance.init(Cipher.ENCRYPT_MODE, key, ivspec);

        String paddedData = addPaddDate(data); 
        byte[] encryptedBytes = instance.doFinal(paddedData.getBytes());
        byte[] encodeBase64 = Base64.encodeBase64(encryptedBytes); 
        String encryptedStr = new String(encodeBase64);

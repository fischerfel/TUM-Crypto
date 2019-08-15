import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class HelloWorld {
    public static void main(String[] args) throws Exception {
        String key = "E1BB465D57CAE7ACDBBE8091F9CE83DF";
        String plaintext = "komal";
        String crp = encrypt(plaintext, key);
        String dec = decrypt(crp, key);

        //String key = "605bd70efed2c6374823b54bbc560b58";
        //String plaintext = "5454545454545454";
        //String crp = encrypt(plaintext, key);
        //String dec = decrypt(crp, key);
        System.out.println("Encrypt:" + crp);
        System.out.println("Decrypt:" + dec);
    }

    private static final String ALGORITMO = "AES/CBC/PKCS5Padding";
    private static final String CODIFICACION = "UTF-8";

    public static String encrypt(String plaintext, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] raw = DatatypeConverter.parseHexBinary(key);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] cipherText = cipher.doFinal(plaintext.getBytes(CODIFICACION));
        byte[] iv = cipher.getIV();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(iv);
        outputStream.write(cipherText);
        byte[] finalData = outputStream.toByteArray();
        String encodedFinalData = DatatypeConverter.printBase64Binary(finalData);
        return encodedFinalData;
    }

    public static String decrypt(String encodedInitialData, String key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        byte[] encryptedData = DatatypeConverter.parseBase64Binary(encodedInitialData);
        byte[] raw = DatatypeConverter.parseHexBinary(key);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        byte[] iv = Arrays.copyOfRange(encryptedData, 0, 16);
        byte[] cipherText = Arrays.copyOfRange(encryptedData, 16, encryptedData.length);
        IvParameterSpec iv_specs = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv_specs);
        byte[] plainTextBytes = cipher.doFinal(cipherText);
        String plainText = new String(plainTextBytes);
        return plainText;
    }
}

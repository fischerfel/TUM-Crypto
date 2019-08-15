import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        File AesKeyFile = new File("./Cipher2PHP/aes.key");
        File InitializationVectorFile = new File("./Cipher2PHP/initialization.vector");
        File EncryptedDataFile = new File("./Cipher2PHP/encrypted.data");
        byte[] AesKeyData = Files.readAllBytes(AesKeyFile.toPath());
        byte[] InitializationVectorData = Files.readAllBytes(InitializationVectorFile.toPath());
        byte[] EncryptedData = Files.readAllBytes(EncryptedDataFile.toPath());

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(AesKeyData, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(InitializationVectorData));

        byte[] result = cipher.doFinal(EncryptedData);
        String decrypted = new String(result);

        System.out.printf("Your data: %s\n", decrypted);
    }
}

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class DecryptionTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        String password = "12345678901234567890";
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "ECB");
        Cipher m_decrypter = Cipher.getInstance("DESede/ECB/ZeroBytePadding");
        m_decrypter.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedText = m_decrypter.doFinal("bdf0baf948bff7e7".getBytes());
        System.out.println(new String(decryptedText));
    }
}

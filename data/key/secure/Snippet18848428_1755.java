import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

public class AesFileIo {
//    private static final String AES_ALGORITHM = "AES/CTR/NoPadding";
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private SecretKeySpec secretKeySpec;
    private IvParameterSpec ivSpec;

    public AesFileIo(byte[] aesKey, byte[] iv) {
        ivSpec = new IvParameterSpec(iv);
        secretKeySpec = new SecretKeySpec(aesKey, "AES");
    }

    public String decrypt(String text) {

        StringBuilder stringBuilder = new StringBuilder(); 
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            byte[] decordedValue =  Base64.decode(text,Base64.DEFAULT);
            String decryptedValue = new String(cipher.doFinal(decordedValue),"UTF-8");
            Log.e("decrypted Value :",decryptedValue);
            return decryptedValue; 
        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.getMessage(), e);
        }
        return stringBuilder.toString();
    }

    public String encrypt(String text) {
        String encryptedValue=null;
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM); 
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            byte[] encValue = cipher.doFinal(text.getBytes());
            encryptedValue = Base64.encodeToString(encValue,Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.getMessage(), e);
        }
        return encryptedValue;
    }
}

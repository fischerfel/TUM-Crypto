import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCBC {
    private byte[] key;
    private byte[] iv;

    private static final String ALGORITHM="AES";

    public AesCBC(byte[] key, byte[] iv) {
        this.key = key;
        this.iv = iv;
    }

    public byte[] encrypt(byte[] plainText) throws Exception{
        SecretKeySpec secretKey=new SecretKeySpec(key,ALGORITHM);
        IvParameterSpec ivParameterSpec=new IvParameterSpec(iv);
        Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,ivParameterSpec);
        return cipher.doFinal(plainText);
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }
}

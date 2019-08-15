import javax.crypto.Cipher;

public abstract class Crypto {

    private static final String CIPHER_ALGORITHM = "AES/CTR/NoPadding";
    private String AesKeyString = "ByWelFHCgFqivFZrWs89LQ==";

    private void setKey() throws NoSuchAlgorithmException{
        byte[] keyBytes;
        keyBytes = Base64.getDecoder().decode(AesKeyString);
        aesKey = new SecretKeySpec(keyBytes, "AES");
    }

    protected byte[] execute(int mode, byte[] target, byte[] iv) 
            throws Exception{
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(mode, aesKey, ivSpec);
        return cipher.doFinal(target);
    }

}

public class AESHelper {

    private final Cipher cipher;
    private final SecretKeySpec key;
    private AlgorithmParameterSpec spec;
    private static final String KEY = "VHJFTFRGJHGHJDhkhjhd/dhfdh=";


    public AESHelper() throws Exception { 
        byte[] keyBytes = KEY.getBytes("UTF-8");
        Arrays.fill(keyBytes, (byte) 0x00);

        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        key = new SecretKeySpec(keyBytes, "AES");
        spec = getIV();
    }

    public AlgorithmParameterSpec getIV() { 
        final byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        return new IvParameterSpec(iv);
    }

    public String encrypt(String plainText) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        String encryptedText = new String(Base64.encode(encrypted, Base64.DEFAULT), "UTF-8");
        return encryptedText; 
    }

    public String decrypt(String cryptedText) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
        byte[] decrypted = cipher.doFinal(bytes);
        String decryptedText = new String(decrypted, "UTF-8");
        return decryptedText; 
    }

}

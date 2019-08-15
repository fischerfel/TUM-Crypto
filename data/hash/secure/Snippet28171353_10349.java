 public class Crypt {

private static final String tag = Crypt.class.getSimpleName();

private static final String characterEncoding = "UTF-8";
private static final String cipherTransformation = "AES/CBC/PKCS5Padding";
private static final String aesEncryptionAlgorithm = "AES";
private static final String key = "e8ffc7e56311679f12b6fc91aa77a5eb";
private static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
private static byte[] keyBytes;

private static Crypt instance = null;


private Crypt(){}



public static Crypt getInstance() {
    if(instance == null){
        instance = new Crypt();
    }

    return instance;
}



public   byte[] encrypt(   byte[] mes)
        throws NoSuchAlgorithmException,
        NoSuchPaddingException,
        InvalidKeyException,
        InvalidAlgorithmParameterException,
        IllegalBlockSizeException,
        BadPaddingException, IOException {

    keyBytes = key.getBytes("UTF-8");
    Log.d(tag,"Long KEY: "+keyBytes.length);
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(keyBytes);
    keyBytes = md.digest();

    Log.d(tag,"Long KEY: "+keyBytes.length);

    AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = null;
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

    byte[] destination = new byte[ivBytes.length + mes.length];
    System.arraycopy(ivBytes, 0, destination, 0, ivBytes.length);
    System.arraycopy(mes, 0, destination, ivBytes.length, mes.length);
    return  cipher.doFinal(destination);

}

public   byte[] decrypt(   byte[] bytes)
        throws NoSuchAlgorithmException,
        NoSuchPaddingException,
        InvalidKeyException,
        InvalidAlgorithmParameterException,
        IllegalBlockSizeException,
        BadPaddingException, IOException, ClassNotFoundException {

    keyBytes = key.getBytes("UTF-8");
    Log.d(tag,"Long KEY: "+keyBytes.length);
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(keyBytes);
    keyBytes = md.digest();
    Log.d(tag,"Long KEY: "+keyBytes.length);

    byte[] ivB = Arrays.copyOfRange(bytes,0,16);
    Log.d(tag, "IV: "+new String(ivB));
    byte[] codB = Arrays.copyOfRange(bytes,16,bytes.length);


    AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivB);
    SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
    return  cipher.doFinal(codB);

}

}

public class AsymetricUtil{

private static final String KEYMODE = "RSA/ECB/PKCS1Padding";
private KeyPairGenerator kpg;
private KeyPair kp;
private PublicKey publicKey;
private PublicKey publicKeyOther = null;
private PrivateKey privateKey;
private byte [] encryptedBytes,decryptedBytes;
private Cipher cipher;

public AsymetricUtil(){
    try{
        cipher = Cipher.getInstance(KEYMODE);
        generateKeys();
    }catch( NoSuchPaddingException| NoSuchAlgorithmException e){
        e.printStackTrace();
    }
}


public void setPublicKey(String key) {

    byte[] data = Base64.decode(key, Base64.NO_WRAP);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
    KeyFactory fact;

    try {
        fact = KeyFactory.getInstance("RSA");
        this.publicKeyOther = fact.generatePublic(spec);

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        e.printStackTrace();
    }

}

public String RSAEncrypt(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    cipher.init(Cipher.ENCRYPT_MODE, publicKeyOther);
    encryptedBytes = cipher.doFinal(plain.getBytes("Utf-8"));
    return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);
}


public String RSADecrypt(final String encryptedStr) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    decryptedBytes = cipher.doFinal(Base64.decode(encryptedStr, Base64.NO_WRAP));
    return new String(decryptedBytes);
}

public void generateKeys() throws NoSuchAlgorithmException {
    kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    kp = kpg.genKeyPair();
    publicKey = kp.getPublic();
    privateKey = kp.getPrivate();
}


/**
 * Convert publicKey to String
 *
 * @return  {String}
 */
public String getKey(){
    return Base64.encodeToString(publicKey.getEncoded(), Base64.NO_WRAP);
}

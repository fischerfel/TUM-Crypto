public class AES {

private static ArrayList<String> uncryptedArrayList = new ArrayList<>();
private static String pinString;

private SecretKeyFactory factory;
private KeySpec spec;
private SecretKey tmp;
private Cipher dcipher;
private byte[] salt, iv, decodedData, decryptedData, pin, pass, encryptedData, encodedData;
private int iterationCount = 1024;
private int keyStrength = 128;
private SecretKey key;
private String magic;
private AlgorithmParameters params;

public AES() {

    try {

        salt = new String("TheBestSaltEvers").getBytes();
        magic = new String("ABCDEFGHIJKLMNOP");
        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        spec = new PBEKeySpec(magic.toCharArray(), salt, iterationCount, keyStrength);
        tmp = factory.generateSecret(spec);
        key = new SecretKeySpec(tmp.getEncoded(), "AES");
        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        params = dcipher.getParameters();
        iv = params.getParameterSpec(IvParameterSpec.class).getIV();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidParameterSpecException e) {
        e.printStackTrace();
    }
}

public byte[] encrypt(String data) {

    try {

        dcipher.init(Cipher.ENCRYPT_MODE, key);
        encryptedData = dcipher.doFinal(data.getBytes("UTF8"));
        encodedData = new Base64().encode(encryptedData);

    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    return encodedData;
}

public String decrypt(byte[] data) {

    String result = null;

    try {

        dcipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        decodedData = new Base64().decode(data);
        decryptedData = dcipher.doFinal(decodedData);

        result = new String(decryptedData, "UTF8");

    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    return result;
}

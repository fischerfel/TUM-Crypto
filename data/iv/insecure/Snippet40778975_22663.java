private static String TRANSFORMATION = "AES/CBC/PKCS5Padding";

private static String ALGORITHM = "AES";
private static String DIGEST = "MD5";

private static Cipher cipher;
private static SecretKey password;
private static IvParameterSpec IVParamSpec;
private final static String pvtkey="GDNBCGDRFSC$%#%=";

//16-byte private key
private static byte[] IV = pvtkey.getBytes();

public PassWordEncryptor() {
    try {

        //Encode digest
        MessageDigest digest;           
        digest = MessageDigest.getInstance(DIGEST);            
        password = new SecretKeySpec(digest.digest(pvtkey.getBytes()), ALGORITHM);

        //Initialize objects
        cipher = Cipher.getInstance(TRANSFORMATION);
        IVParamSpec = new IvParameterSpec(IV);

    } catch (NoSuchAlgorithmException e) {
        Log.i(Lams4gApp.TAG, "No such algorithm " + ALGORITHM);
    } catch (NoSuchPaddingException e) {
        System.out.println( "No such padding PKCS7"+ e);
    }
}
/**
Encryptor.

@text String to be encrypted
@return Base64 encrypted text

*/
public String encrypt(byte[] text) {

    byte[] encryptedData;

    try {

        cipher.init(Cipher.ENCRYPT_MODE, password, IVParamSpec);
        encryptedData = cipher.doFinal(text);

    } catch (InvalidKeyException e) {
        System.out.println( "Invalid key  (invalid encoding, wrong length, uninitialized, etc)."+ e);
        return null;
    } catch (InvalidAlgorithmParameterException e) {
        System.out.println( "Invalid or inappropriate algorithm parameters for " + ALGORITHM+ e);
        return null;
    } catch (IllegalBlockSizeException e) {
        System.out.println( "The length of data provided to a block cipher is incorrect"+ e);
        return null;
    } catch (BadPaddingException e) {
        System.out.println( "The input data but the data is not padded properly."+ e);
        return null;
    }               
    return Base64.encodeToString(encryptedData,Base64.DEFAULT);

}

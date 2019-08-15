private static String TRANSFORMATION = "AES/ECB/NoPadding";
private static String ALGORITHM = "AES";
private static String DIGEST = "MD5";
byte[] encryptedData;

public RijndaelCrypt(String password,String plainText) {

    try {

        //Encode digest
        MessageDigest digest;           
        digest = MessageDigest.getInstance(DIGEST);            
        _password = new SecretKeySpec(digest.digest(password.getBytes()), ALGORITHM);

        //Initialize objects
        _cipher = Cipher.getInstance(TRANSFORMATION);

       _cipher.init(Cipher.ENCRYPT_MODE, _password);
        encryptedData = _cipher.doFinal(text);

    } catch (InvalidKeyException e) {
        Log.e(TAG, "Invalid key  (invalid encoding, wrong length, uninitialized, etc).", e);
        return null;
    } catch (InvalidAlgorithmParameterException e) {
        Log.e(TAG, "Invalid or inappropriate algorithm parameters for " + ALGORITHM, e);
        return null;
    } catch (IllegalBlockSizeException e) {
        Log.e(TAG, "The length of data provided to a block cipher is incorrect", e);
        return null;
    } catch (BadPaddingException e) {
        Log.e(TAG, "The input data but the data is not padded properly.", e);
        return null;
    }               

    return Base64.encodeToString(encryptedData,Base64.DEFAULT);
}

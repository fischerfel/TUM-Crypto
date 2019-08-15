public String decrypt(String dataToDecrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException 
{

    byte[] encryptedCombinedBytes = Base64.decodeBase64(dataToDecrypt.getBytes());

    String saltKey = "dfkjsadfinewdfadsfkmeoinmsdflksdflk";
    String password = "52";
    String IVKey = "@EUBRHDFBFG8867";

    PBKDF2Parameters p = new PBKDF2Parameters("HmacSHA256", "ASCII", saltKey.getBytes(), 8);

    byte[] mEncryptedPassword = new PBKDF2Engine(p).deriveKey(password);


    byte[] ivbytes = Arrays.copyOfRange(IVKey.getBytes(), 0, 16);

    SecretKeySpec mSecretKeySpec = new SecretKeySpec(mEncryptedPassword, "AES");

    Cipher mCipher = Cipher.getInstance("AES/CBC/NoPadding");

    mCipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec, new IvParameterSpec(ivbytes));

    byte[] encryptedTextBytes = Arrays.copyOfRange(encryptedCombinedBytes, 16, encryptedCombinedBytes.length);

    byte[] decryptedTextBytes = mCipher.doFinal(encryptedTextBytes);

    return new String(decryptedTextBytes, "UTF-8");
}

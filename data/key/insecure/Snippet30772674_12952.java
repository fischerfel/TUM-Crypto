 public String encrypt(String textToEncrypt) {
    String encryptedpassword = null;
    try {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        password1 = textToEncrypt.getBytes();
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipherpass = Cipher.getInstance("DES/CTR/NoPadding", "BC");
        cipherpass.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        cipherTextPassword = new byte[cipherpass.getOutputSize(password1.length)];
        passLength = cipherpass.update(password1, 0, password1.length, cipherTextPassword, 0);
        passLength += cipherpass.doFinal(cipherTextPassword, passLength);
        encryptedpassword = new String(cipherTextPassword);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | ShortBufferException | IllegalBlockSizeException | BadPaddingException ex) {

    }
    return encryptedpassword;
}
 byte[] password1;
byte[] emailpass1;
byte[] keyBytes = "12345678".getBytes();
byte[] ivBytes = "input123".getBytes();
SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
Cipher cipherEmailpass, cipherpass;
byte[] cipherTextPassword;
byte[] cipherEmailTextPassword;
int passLength, emailpassLength;

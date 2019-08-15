 public String DesDecryptPin(String pin, String encryptKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

    String UNICODE_FORMAT = "UTF8";
    String decryptedPinText = null;

    byte[] hexConvert = hexStringtoByteArray(encryptKey);

    SecretKey desKey = null;
    KeySpec desKeySpec = new DESedeKeySpec(hexConvert); // Exception HERE
    Cipher desCipher;
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
    desCipher = Cipher.getInstance("DES/ECB/NoPadding");
    try {
        desKey = skf.generateSecret(desKeySpec);
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }

    desCipher.init(Cipher.DECRYPT_MODE, desKey);
    byte[] decryptPin = desCipher.doFinal(pin.getBytes());
    decryptedPinText = new String(decryptPin, "UTF-8");

    return decryptedPinText;
}

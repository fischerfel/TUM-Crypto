public static String decryptText(String textToDecrypt) {
    try {
        byte[] decodedValue = Base64.decodeBase64(textToDecrypt.getBytes());

        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ips = new IvParameterSpec(iv);

        byte[] input = textToDecrypt.getBytes();

        Cipher cipher = Cipher.getInstance(ENCRYPTION_METHOD);

        // decryption pass
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, ips);
        byte[] plainText = cipher.doFinal(decodedValue);

        return new String(plainText);
    } catch (Exception e) {
        e.printStackTrace();
        Log.e(TAG, "Decipher error for " + textToDecrypt, e);
    }

    return "";
}

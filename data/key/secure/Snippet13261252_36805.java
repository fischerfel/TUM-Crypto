public static String decryptText(String textToDecrypt) {
    try {

        byte[] base64TextToDecrypt = Base64.encodeBase64(textToDecrypt.getBytes("UTF-8"));

        byte[] guid = "fjakdsjkld;asfj".getBytes("UTF-8");

        byte[] iv = new byte[16];
        System.arraycopy(guid, 0, iv, 0, guid.length);
        IvParameterSpec ips = new IvParameterSpec(iv);

        byte[] secret = DECRYPTION_SECRET_HASH.getBytes("UTF-8");
        SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // decryption pass
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ips);
        byte[] converted = cipher.doFinal(base64TextToDecrypt);
        System.out.println(new String(converted));

    } catch (Exception e) {
        e.printStackTrace();
        Log.e(TAG, "Decipher error for " + textToDecrypt, e);
    }
    return "";
}

private static String decrypt(String key, String initVector, String dataToDecrypt) {
    try {
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        String safeString = dataToDecrypt.replace('-', '+').replace('_', '/');
        byte[] decodedString = Base64.decodeBase64(safeString);

        byte[] original = cipher.doFinal(decodedString);

        return new String(original);
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return null;
}

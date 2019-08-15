public static String aes128(String key, String data, final int direction) {
    SecureRandom rand = new SecureRandom(key.getBytes());
    byte[] randBytes = new byte[16];
    rand.nextBytes(randBytes);
    SecretKey encKey = new SecretKeySpec(randBytes, "AES");

    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("AES");
        cipher.init((direction == ENCRYPT ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE), encKey);
    } catch (InvalidKeyException e) {
        return null;
    } catch (NoSuchPaddingException e) {
        return null;
    } catch (NoSuchAlgorithmException e) {
        return null;
    }

    try {
        if (direction == ENCRYPT) {
            byte[] encVal = cipher.doFinal(data.getBytes());
            String encryptedValue = Base64.encode(encVal);
            return encryptedValue;
        } else {
            byte[] dataBytes = Base64.decode(data);
            byte[] encVal = cipher.doFinal(dataBytes);
            return new String(encVal);
        }
    } catch (NullPointerException e) {
        return null;
    } catch (BadPaddingException e) {
        return null;
    } catch (IllegalBlockSizeException e) {
        return null;
    }
}

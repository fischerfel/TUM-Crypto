public static String aesDecrypt(String data, String password) {
    try {
        String aesKey = getAesKey(password);
        byte[] keyValue = Base64.decode(aesKey, Base64.NO_WRAP);
        SecretKey key = new SecretKeySpec(keyValue, "AES");
        Cipher c = Cipher.getInstance(AES_ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] dataB = Base64.decode(data, Base64.NO_WRAP);
        byte[] decVal = c.doFinal(dataB);
        return new String(decVal);
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    }
    return null;
}

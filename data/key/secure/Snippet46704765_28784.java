public static byte[] encodeFile(byte[] secretKey, byte[] fileData) {
    SecretKeySpec skeySpec = new SecretKeySpec(secretKey, "AES");
    byte[] encrypted = null;
    try {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        encrypted = cipher.doFinal(fileData);

        // Now write your logic to save encrypted data to sdcard here
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (Exception e){
        e.printStackTrace();
    }
    return encrypted;
}

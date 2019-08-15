public static byte[] decodeFile(byte[] key, byte[] fileData) {
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    byte[] decrypted = null;
    try {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        decrypted = cipher.doFinal(fileData);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException | BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch(Exception e){
        // for all other exception
        e.printStackTrace();
    }
    return decrypted;
}

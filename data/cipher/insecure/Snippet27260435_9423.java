public String encrypt(String data) {

    try {

        SecretKeySpec KS = new SecretKeySpec(mKeyData, "Blowfish");

        Cipher cipher = Cipher.getInstance("Blowfish/CBC/ZeroBytePadding"); // PKCS5Padding
        cipher.init(Cipher.ENCRYPT_MODE, KS, new IvParameterSpec(mIv));
        return bytesToHex(cipher.doFinal(data.getBytes()));       

    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
    return null;
}

public static void testAndroidAesCfbDecrypther() {

    Cipher AESCipher;
    final String password = "th3ke1of16b1t3s0"; //password
    final byte[] IV = DatatypeConverter.parseHexBinary("aabbccddeeff3a1224420b1d06174748"); //vector

    final String expected = "This is clear text right now";
    final byte[] encrypted1 = DatatypeConverter.parseHexBinary("a1ea8e1c4d8579b84e3e8d48d17fe916a70079b1bdc75841667cc15f");
    final byte[] encrypted2 = DatatypeConverter.parseHexBinary("73052b25306059dda5d6880aa873383124448a38bcb3a769f6aed2f5");

    try {
        byte[] key = password.getBytes("US-ASCII");
        key = Arrays.copyOf(key, 16); // use only first 128 bit


        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        IvParameterSpec IVSpec = new IvParameterSpec(IV);

        AESCipher = Cipher.getInstance("AES/CFB/NoPadding"); //Tried also with and without "BC" provider

        AESCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IVSpec);

        byte[] dec1 = AESCipher.update(encrypted1);
        String r = new String(dec1);
        assertEquals(expected, r); //assert fail here

        byte[] dec2 = AESCipher.update(encrypted2);
        r = new String(dec2);
        assertEquals(expected, r);

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }

}

private static void assertEquals(String left, String right) {
    System.out.println(left+":"+right);
    System.out.println(left.equals(right));
}

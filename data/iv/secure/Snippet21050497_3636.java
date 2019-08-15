public static String encryptPadding(String plaintext, byte[] salt) {
    try {

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, SKey, ivSpec);

        byte[] cipherText = cipher.doFinal(PlainText.getBytes("UTF-8"));

        cyphertext = Base64.encodeToString(cipherText, Base64.DEFAULT);
        edit_txt_enc_string.setText(cyphertext);
        return cyphertext;
    } catch (GeneralSecurityException e) {
        throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
    }
}

public static String decryptPadding(String ctext, byte[] salt) {
    try {

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, SKey, ivSpec);

        byte[] plaintxt = cipher.doFinal(Base64.decode(cyphertext, Base64.DEFAULT));

        PlainText = new String(plaintxt, "UTF-8");
        edit_txt_dec_string.setText(PlainText);
        return PlainText;
    } catch (GeneralSecurityException e) {
        throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
    }
}

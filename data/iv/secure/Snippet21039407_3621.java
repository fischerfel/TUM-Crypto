public String encryptPadding(String plaintext, byte[] salt) {
    try {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, SKey);

        byte[] cipherText = cipher.doFinal(PlainText.getBytes("UTF-8"));

        cyphertext = String.format("%s%s%s", toBase64(salt), "]",
                toBase64(cipherText));
        edit_txt_enc_string.setText(cyphertext);
        return cyphertext;
    } catch (GeneralSecurityException e) {
        throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
    }
}

public String decryptPadding(String ctext, byte[] salt) {
    try {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, SKey, ivSpec);

        byte[] plaintxt = cipher.doFinal(cyphertext.getBytes("UTF-8"));

        PlainText = String.format("%s%s%s", fromBase64(salt), "]",
                fromBase64(plaintxt));
        edit_txt_dec_string.setText(PlainText);
        return PlainText;
    } catch (GeneralSecurityException e) {
        throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
    }
}

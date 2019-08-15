public String encript(String dataToEncrypt)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    // I'm using AES encription

    if(!dataToEncrypt.equals("")){
        String key = "FMVWf8d_sm#fz";

        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k;
        try {
            k = new SecretKeySpec(key.getBytes(), "AES");
            c.init(Cipher.ENCRYPT_MODE, k);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return new String(c.doFinal(Base64.decode(dataToEncrypt)));
    }
    return "";
}

public String decript(String encryptedData)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    if(!encryptedData.equals("")){
        String key = "FMVWf8d_sm#fz";

        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(Base64.decode(key), "AES");
        c.init(Cipher.DECRYPT_MODE, k);
        return new String(c.doFinal(Base64.decode(encryptedData)));
    }
    return "";
}

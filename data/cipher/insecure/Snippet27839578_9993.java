public AES()
    {
        try
        {
            Security.addProvider(new BouncyCastleProvider());
            cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public String doDecrypt(String key, String cipherText)
    {
        try
        {
            byte[] raw = key.getBytes(Charset.forName("UTF-8"));
            SecretKeySpec skey = new SecretKeySpec(raw, "AES");
            cipher.init(Cipher.DECRYPT_MODE, skey );
            return new String(cipher.doFinal(Base64.decode(cipherText,Base64.DEFAULT)), Charset.forName("UTF-8"));

        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String doEncrypt(String key, String plainText)
    {
        try
        {
            byte[] raw = key.getBytes(Charset.forName("UTF-8"));
            SecretKeySpec skey = new SecretKeySpec(raw, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, skey );
            return Base64.encodeToString(cipher.doFinal(plainText.getBytes(Charset.forName("UTF-8"))),Base64.DEFAULT);

        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}

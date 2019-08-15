    public String decrypt(String enc) throws Exception
    {
        Key key = k;
        Cipher crypt = Cipher.getInstance("AES");
        crypt.init(Cipher.DECRYPT_MODE,key);
        byte[] decrypt = crypt.doFinal(Base64.getMimeDecoder().decode(enc));
        return new String(decrypt);
    }

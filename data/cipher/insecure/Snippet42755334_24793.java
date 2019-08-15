    public String decrypt(String enc) throws Exception
    {
        Key key = k;
        Cipher crypt = Cipher.getInstance("AES");
        crypt.init(Cipher.DECRYPT_MODE,key);
        byte[] decrypt = crypt.doFinal(enc.getBytes());
        return new String(decrypt);
    }

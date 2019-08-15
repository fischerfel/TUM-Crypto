    public byte[] encryptBytes(String pwd, byte[] key) throws Exception {

        byte[] cipherText = null;
        PublicKey pk;

        try {
            byte[] dataBytes = pwd.getBytes();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            pk = keyFactory.generatePublic(new X509EncodedKeySpec(key));
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            cipherText = cipher.doFinal(dataBytes);
        } (..... etc etc)
            return cipherText;

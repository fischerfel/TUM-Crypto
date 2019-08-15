byte[] u1Encrypted = RSAEncrypt(String.valueOf(inputEmail.getText()).getBytes());

 public byte[] RSAEncrypt(byte[] data) {
    try {
        PublicKey pubKey = ReadPublicKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(data);
        return cipherData;
    }        
    catch (Exception ex)
    {
        Exception e = ex;
        return null;
    }

}


private PublicKey ReadPublicKey() throws IOException {
    try {
        AssetManager assetManager = getAssets();
        InputStream in = assetManager.open("public.key");
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));

        try {

            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }
    catch (Exception ex)
    {
        Exception e = ex;
        return null;
    }
}

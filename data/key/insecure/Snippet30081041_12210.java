    import javax.crypto.spec.SecretKeySpec;

    class Encrypter
{
    String alg = "AES";
    Key key;
    Cipher cipher;

    public Encrypter() throws Exception
    {
         //key = KeyGenerator.getInstance(alg).generateKey();
key = new SecretKeySpec("C@ThA$rpma#EsP!!".getBytes(), "AES");
         cipher = Cipher.getInstance(alg);
    }

    public byte[] encrypt(String str) throws Exception
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inBytes = str.getBytes("UTF-16");

        return cipher.doFinal(inBytes);
    }

    public String decrypt(byte[] enBytes) throws Exception
    {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(enBytes);
        String rData = new String(bytes, "UTF-16");

        return rData;
    }
}

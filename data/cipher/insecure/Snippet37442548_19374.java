public class AES {
    private String a= "AES/ECB/NoPadding";
    private byte[] key;
    Cipher c;
    public AES(byte [] key) throws NoSuchAlgorithmException, NoSuchPaddingException{
            this.key = key;
            c = Cipher.getInstance(a);
        }
public String encrypt(byte[] Data) throws Exception{
        Key k = new SecretKeySpec(key, "AES");
        c.init(Cipher.ENCRYPT_MODE, k);
        byte[] encoded = c.doFinal(Data);
        String encrypted= new String(encoded);
        return encrypted;

    }
public String decrypt(byte[] v) throws Exception{
        Key k = new SecretKeySpec(key, "AES");
        if(v.length%16!=0)
            return null;
        c.init(Cipher.DECRYPT_MODE, k);
        byte[] decv = c.doFinal(v);
        String decrypted = new String(decv);
        return decrypted;
    }
}

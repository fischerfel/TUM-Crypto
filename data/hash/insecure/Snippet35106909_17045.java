public class AES_Cipher {

    private String keyString;
    private byte[] byteKey;
    private SecretKey key;
    Cipher c;

    public AES_Cipher(String keyString){
        this.keyString = keyString.toString();
    }

    public void init() throws InitializtionFailedException{
        try{
            c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byteKey = keyString.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byteKey = sha.digest(byteKey);
            byteKey = Arrays.copyOf(byteKey, 16);
            key = new SecretKeySpec(byteKey, "AES");

        }catch(NoSuchAlgorithmException e){
            throw new InitializtionFailedException();
        }
}

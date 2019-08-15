public final class StringFunc {
    final static String key = "jb2a19ou79rws6zknjlr803fvfgiyp1k";
    final static String algorithm = "AES/CBC/PKCS7Padding";
    final static String iv = "hod74ty97wr97g83";
    private static Cipher cipher = null;
    private static SecretKeySpec skeySpec = null;
    private static IvParameterSpec  ivSpec = null;

    private static void setUp(){
        try{
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
            skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            ivSpec = new IvParameterSpec(iv.getBytes());
            cipher = Cipher.getInstance(algorithm);
        }catch(NoSuchAlgorithmException | NoSuchPaddingException ex){
        }
    }

    public static String encrypt(String str){
        try{
            Integer strL = (int) Math.ceil(str.length() / 8.0);
            Integer strB = strL*8;
            str = padRight(str, '', strB);
            setUp();
            try {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
            } catch (InvalidAlgorithmParameterException ex) {
                return "";
            }
            byte[] enc = cipher.doFinal(str.getBytes());
            return new String(Base64.encodeBase64(enc));
        }catch(InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex){
            return "";            
        }
    }

    public static String padRight(String msg, char x, int l) {
        String result = "";
        if (!msg.isEmpty()) {
            for (int i=0; i<(l-msg.length()); i++) {
                result = result + x;
            }
            result = msg + result;
        }
        return result;
    }
}

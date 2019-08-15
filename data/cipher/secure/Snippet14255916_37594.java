public class Test {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    public static void main(String[] args) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

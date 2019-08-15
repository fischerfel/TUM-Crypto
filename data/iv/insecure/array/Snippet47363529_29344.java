public class TripleDes {

    private static byte[] keyAsBytes = new byte[]{50, -72, 45, -30, 78, 0, -73, -24, 17, 18, 32, 25, -34, -32, 90, 122, 65, 62, 16, -85, 20, 0, -73, -24};

    private static byte[] keyiv = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};

    private static String CODING_FORMAT = "desede/CBC/PKCS7Padding";
    private static String CODING_TYPE = "desede";

    public TripleDes() {
    }



    public static String encryptCBCHEX(byte[] data) {
        try {
            Key deskey = null;
            String encryptedString1 = byteArrayToHex(data);
            DESedeKeySpec spec = new DESedeKeySpec(keyAsBytes);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(CODING_TYPE);
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance(CODING_FORMAT);
            IvParameterSpec ips = new IvParameterSpec(keyiv);
            cipher.init(1, deskey, ips);
            byte[] bout = cipher.doFinal(data);
            return byteArrayToHex(bout);
        } catch (Exception var8) {
            System.out.println("methods qualified name" + var8);
            return null;
        }
    }

}

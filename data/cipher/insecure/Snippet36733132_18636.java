public class AESEncryptUtil {

    private static AESEncryptUtil instance = new AESEncryptUtil();
    private String password = "123456";
    private Key key;
    private Cipher cipher;

    public AESEncryptUtil(){
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            key = new SecretKeySpec(enCodeFormat, "AES");
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte[] encrypt(String content) throws Exception {
        byte[] byteContent = content.getBytes("utf-8");
        instance.cipher.init(Cipher.ENCRYPT_MODE, instance.key);
        byte[] result = instance.cipher.doFinal(byteContent);
        return result;
    }
    public static byte[] decrypt(byte[] content) throws Exception {
        instance.cipher.init(Cipher.DECRYPT_MODE, instance.key);
        byte[] result = instance.cipher.doFinal(content);
        return result;
    }
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
    public static String getNonce() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    public static void main(String[] args) throws Exception {
        String content = "test";  
        System.out.println("content: " + content);  
        byte[] encryptResult = encrypt(content);  
        String encryptResultStr = parseByte2HexStr(encryptResult);  
        System.out.println("encryptResultStr: " + encryptResultStr);  
        byte[] decryptFrom = parseHexStr2Byte(encryptResultStr);  
        byte[] decryptResult = decrypt(decryptFrom);  
        System.out.println("decryptResult: " + new String(decryptResult));  
    }
} 

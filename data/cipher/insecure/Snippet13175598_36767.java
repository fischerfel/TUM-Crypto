public class Main {
    public static void main(String[] args) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        byte[] keyByte = new byte[8];
        byte[] data = new byte[8];
        Key key = new SecretKeySpec(keyByte, "DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(data);
        StringBuilder str = new StringBuilder();
        for (byte b : result) {
             str.append(Integer.toHexString(0xff & b)).append(' ');
        }
        System.out.println(str);
    }
}

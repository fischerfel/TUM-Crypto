public class Main {

    public static void main(String [] args) {
        byte [] code = encode("ABCDEFGHIJKLMNOP".getBytes(), "1111111122222222111111112222222211111111222222221111111122222222", "66666666555555556666666655555555");
    }

    private static byte[] toByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        int a;
        int b;
        for (int i = 0; i < len; i += 2) {
            a = (Character.digit(s.charAt(i), 16) << 4);
            b = Character.digit(s.charAt(i+1), 16);
            int n = (Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16);
                data[i / 2] = (byte) (n);
        }
        return data;
    }

    private static byte[] encode(byte[] toEncrypt, String skey, String siv)
    {
        byte[] key = toByteArray(skey);
        byte[] iv = toByteArray(siv);

        byte[] array = new byte[toEncrypt.length];

        Cipher cipher;

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE,  new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            array = cipher.doFinal(array);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return array;
    }
}

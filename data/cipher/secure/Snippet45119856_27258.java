import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class encdec {
    public static void main(String[] args) throws Exception {

        String iv   = "1234567890123456";
        String key  = "abcdefghijklmnop";
        String text = "simple text";

        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher c1 = Cipher.getInstance("AES/CFB/NoPadding");
        c1.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = c1.doFinal(text.getBytes());

        System.out.println(toHex(encrypted));

        Cipher c2 = Cipher.getInstance("AES/CFB/NoPadding");
        c2.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = c2.doFinal(encrypted);

        System.out.println(toHex(decrypted));
        System.out.println(new String(decrypted));
    }

    // https://stackoverflow.com/a/1040876
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
    public static String toHex(byte[] bytes)
    {
        char[] c = new char[bytes.length*2];
        int index = 0;
        for (byte b : bytes)
        {
            c[index++] = HEX_DIGITS[(b >> 4) & 0xf];
            c[index++] = HEX_DIGITS[b & 0xf];
        }
        return new String(c);
    }
}

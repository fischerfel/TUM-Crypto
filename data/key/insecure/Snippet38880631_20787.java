import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Test3DES {
    public static void main(String[] args) throws Exception  {
        //byte length has to be mutiple of 8!
        String plaintext = "Attack at dawn!!";
        byte[] plainBytes = plaintext.getBytes("UTF-8");
        byte[] encrypted = encrypt(plainBytes);
        byte[] decrypted = decrypt(encrypted);

        System.out.println("Original message: ");
        System.out.printf("Text: %s%n", plaintext);
        System.out.printf("Raw bytes: %s%n", toHexString(plainBytes));
        System.out.println("---");
        System.out.println("Encrypted message: ");
        System.out.printf("Text: %s%n", new String(encrypted, "UTF-8"));
        System.out.printf("Raw bytes: %s%n", toHexString(encrypted));
        System.out.println("---");
        System.out.println("Decrypted message: ");
        System.out.printf("Text: %s%n", new String(decrypted, "UTF-8"));
        System.out.printf("Raw bytes: %s%n", toHexString(decrypted));
    }

    private static String toHexString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    private static byte[] encrypt(byte[] message) throws Exception {
        Cipher encr1, decr2, encr3;
        SecretKeySpec kL, kR, tmp;

        kL = new SecretKeySpec(new byte[] {0, 1, 2, 3, 4, 5, 6, 7}, "DES");
        kR = new SecretKeySpec(new byte[] {8, 9, 10, 11, 12, 13, 14, 15}, "DES");

        encr1 = Cipher.getInstance("DES/ECB/NoPadding");
        decr2 = Cipher.getInstance("DES/ECB/NoPadding");
        encr3 = Cipher.getInstance("DES/ECB/NoPadding");

        encr1.init(Cipher.ENCRYPT_MODE, kL);
        decr2.init(Cipher.DECRYPT_MODE, kR);
        encr3.init(Cipher.ENCRYPT_MODE, kL);

        return encr3.doFinal( decr2.doFinal( encr1.doFinal(message) ) );
    }

    private static byte[] decrypt(byte[] message) throws Exception {
        Cipher decr1, encr2, decr3;
        SecretKeySpec kL, kR;

        kL = new SecretKeySpec(
            new byte[] {0, 1, 2, 3, 4, 5, 6, 7},
            "DES"
        );
        kR = new SecretKeySpec(
            new byte[] {8, 9, 10, 11, 12, 13, 14, 15},
            "DES"
        );

        decr1 = Cipher.getInstance("DES/ECB/NoPadding");
        encr2 = Cipher.getInstance("DES/ECB/NoPadding");
        decr3 = Cipher.getInstance("DES/ECB/NoPadding");

        decr1.init(Cipher.DECRYPT_MODE, kL);
        encr2.init(Cipher.ENCRYPT_MODE, kR);
        decr3.init(Cipher.DECRYPT_MODE, kL);

        return decr3.doFinal( encr2.doFinal( decr1.doFinal(message) ) );
    }
}

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;


public class AES {
    static String IV = "fedcba9876543210";
    static String plaintext = "password"; /*Note null padding*/

    //static String encryptionKey = "0123456789abcdef0123456789abcdef";
    static String encKey = "cf6000c7452f9487cc03cd721a3f18482e43cdcc37c380e062193c489f968d04";
    static String encryptionKey = "";
    public static void main(String [] args) {
        try {
            System.out.print(plaintext.length() + "\n");
            if (plaintext.length() < 16) {
                int pad = 16 - plaintext.length();
                String repeated = new String(new char[pad]).replace("\0", "\0");
                plaintext = plaintext + repeated;
            }
            System.out.println("==Java==");
            System.out.println("plain:   " + plaintext);
            System.out.print(plaintext.length() + "\n");

            encryptionKey = hexToAscii(encKey);
            //encryptionKey = "0123456789abcdef0123456789abcdef";

            byte[] cipher = encrypt(plaintext, encryptionKey);
            String a = byteArrayToHex(cipher);
            System.out.print(a + "\n");

            System.out.print("cipher:  ");
            for (int i=0; i<cipher.length; i++)
                System.out.print(new Integer(cipher[i])+" ");
            System.out.println("");

            String decrypted = decrypt(cipher, encryptionKey);

            System.out.println("decrypt: " + decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(String plainText, String string) throws Exception {
        //System.out.println("encrypting" + encryptionKey.length());


// Check length, in characters
        System.out.println("Length of string is " + string.length()); // prints "11"

// Check encoded sizes
        final byte[] utf8Bytes = string.getBytes("UTF-8");
        System.out.println(utf8Bytes.length); // prints "11"

        final byte[] utf16Bytes= string.getBytes("UTF-16");
        System.out.println(utf16Bytes.length); // prints "24"

        final byte[] utf32Bytes = string.getBytes("UTF-32");
        System.out.println(utf32Bytes.length); // prints "44"

        final byte[] isoBytes = string.getBytes("ISO-8859-1");
        System.out.println(isoBytes.length); // prints "11"

        final byte[] winBytes = string.getBytes("CP1252");
        System.out.println(winBytes.length); // prints "11"

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        System.out.println("enc 2");

        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        System.out.println("enc 3");

        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        System.out.println("enc 4");
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static String hexToAscii(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n / 2);
        for (int i = 0; i < n; i += 2) {
            char a = s.charAt(i);
            char b = s.charAt(i + 1);
            sb.append((char) ((hexToInt(a) << 4) | hexToInt(b)));
        }
        return sb.toString();
    }


    public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText),"UTF-8");
    }
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    private static int hexToInt(char ch) {
        if ('a' <= ch && ch <= 'f') { return ch - 'a' + 10; }
        if ('A' <= ch && ch <= 'F') { return ch - 'A' + 10; }
        if ('0' <= ch && ch <= '9') { return ch - '0'; }
        throw new IllegalArgumentException(String.valueOf(ch));
    }
}

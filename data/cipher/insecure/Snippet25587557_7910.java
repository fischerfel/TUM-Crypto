import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;
import javax.xml.bind.DatatypeConverter;

public class Test11 {

    public static String brutToHexa(byte[] t) {
        StringBuilder sb = new StringBuilder(t.length * 2);
        for (int i = 0; i < t.length; i++) {

            int v = t[i] & 0xff;
            if (v < 16) {

                sb.append('0');

            }

            sb.append(Integer.toHexString(v));//.append("-");

        }

        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                            + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] encrypter(final String message, SecretKey cle)
                    throws NoSuchAlgorithmException, NoSuchPaddingException,
                    InvalidKeyException, IllegalBlockSizeException, BadPaddingException, java.security.InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, cle);
        byte[] donnees = message.getBytes();

        return cipher.doFinal(donnees);
    }

    public static String decrypter(final byte[] donnees, SecretKey cle)
                    throws NoSuchAlgorithmException, NoSuchPaddingException,
                    InvalidKeyException, IllegalBlockSizeException, BadPaddingException, java.security.InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, cle);

        return new String(cipher.doFinal(donnees));
    }

    public static void main(String[] args) throws java.security.InvalidKeyException {

        final String message = "Java is the best";

        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            SecretKey cle = keyGen.generateKey();

            String hexKey = brutToHexa(cle.getEncoded());

            byte[] enc = encrypter(message, cle);
            System.out.print("encrypted text : ");
            System.out.println(DatatypeConverter.printBase64Binary(enc));

            String dec = decrypter(enc, cle);
            System.out.println("decrypted text : " + dec);

            decryptWith(hexKey, brutToHexa(enc));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                        IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

    public static void decryptWith(String hexKey, String hexMessage) throws java.security.InvalidKeyException {
        try {

            byte[] byteKey = hexStringToByteArray(hexKey);
            SecretKey secretKey = new SecretKeySpec(byteKey, "AES");

            byte[] message = hexStringToByteArray(hexMessage);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            String dec = new String(cipher.doFinal(message));
            System.out.println("texte decrypte : " + dec);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }
}

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.util.encoders.Hex;

public class CipherAES {

    public static void brutToHexa(byte[] t) {
        byte[] tab = Hex.encode(t);
        System.out.print("secret key : ");
        for (int i = 0; i < tab.length; i++) {
            System.out.print((char) tab[i] + "");
        }
        System.out.println();
    }

    public static byte[] encrypter(final String message, SecretKey cle)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, cle);
        byte[] donnees = message.getBytes();

        return cipher.doFinal(donnees);
    }

    public static String decrypter(final byte[] donnees, SecretKey cle)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, cle);

        return new String(cipher.doFinal(donnees));
    }

    public static void main(String[] args) {

        final String message = "Java is the best";

        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            SecretKey cle = keyGen.generateKey();
            brutToHexa(cle.getEncoded());

            byte[] enc = encrypter(message, cle);
            System.out.print("encrypted text : ");
            System.out.println(DatatypeConverter.printBase64Binary(enc));

            String dec = decrypter(enc, cle);
            System.out.println("decrypted text : " + dec);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }
}

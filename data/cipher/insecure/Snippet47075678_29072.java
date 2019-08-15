import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class Main {

private static final String ALGO_NAME = "DESede/ECB/NoPadding";
private static final char[] HEX_DIGIT = { '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}


public static void main(String[] args) throws Exception {

    String defKey = "CA:FE:BA:BE:FF:FF:FF:FF:XY:XY+1:XY+2:XY+3:XY+4:XY+5:FF:FF:WZ:WZ+1:WZ+2:WZ+3:WZ+4:WZ+5:FF:FF";
    String encryptedText = "6t8Z5bKl5ybJL+MiFerNBmiTDS7wlTEUdWNwJJApWmQ=";
    byte[] input = Base64.getDecoder().decode(encryptedText);

    for (int i = 0; i < 251; i++)
        for (int j = 0; j < 251; j++) {
            String[] split = defKey.split(":");
            for (int k = 0; k < 6; k++) {
                String a = HEX_DIGIT[(i + k) / 16] + "" + HEX_DIGIT[(i + k) % 16];
                split[8 + k] = a;
                a = HEX_DIGIT[(j + k) / 16] + "" + HEX_DIGIT[(j + k) % 16];
                split[16 + k] = a;
            }

            String secretKey = "";
            for (String q: split) {
                secretKey += q;
            }

            DESedeKeySpec keySpec = new DESedeKeySpec(hexStringToByteArray(secretKey));
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
            SecretKey key = factory.generateSecret(keySpec);


            Cipher cipher = Cipher.getInstance(ALGO_NAME);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = cipher.doFinal(input);
            String decrypted = new String (plainText);

            if (decrypted.toLowerCase().contains("the"))
                System.out.println(decrypted);
        }
}

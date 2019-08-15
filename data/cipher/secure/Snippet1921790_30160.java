import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncTest {

    public static void main(String[] args) throws Exception {
        System.out.println("Testing method using secret key");

        String plaintext = "hello world";

        SecretKey aesKlic = KeyGenerator.getInstance("AES").generateKey();
        SecretKey desKlic = KeyGenerator.getInstance("DES").generateKey();
        SecretKey desedeKlic = KeyGenerator.getInstance("DESede").generateKey();
        SecretKey rc2Klic = KeyGenerator.getInstance("RC2").generateKey();
        SecretKey blowfishKlic = KeyGenerator.getInstance("Blowfish").generateKey();

        StringEncrypter[] ciphers = new StringEncrypter[] {
            new StringEncrypter(aesKlic, aesKlic.getAlgorithm()), 
            new StringEncrypter(desKlic, desKlic.getAlgorithm()), 
            new StringEncrypter(desedeKlic, desedeKlic.getAlgorithm()), 
            new StringEncrypter(rc2Klic, rc2Klic.getAlgorithm()), 
            new StringEncrypter(blowfishKlic, blowfishKlic.getAlgorithm())
        };

        byte[][] ciphertexts = new byte[ciphers.length][];

        int i = 0;
        for (StringEncrypter cipher : ciphers) {
            ciphertexts[i] = cipher.encrypt(plaintext);
            System.out.println(cipher.getAlgorithm() + " encrypted: " + hexEncode(ciphertexts[i]));
            i++;
        }

        System.out.println();

        i = 0;
        for (StringEncrypter cipher : ciphers) {
            System.out.println(cipher.getAlgorithm() + " decrypted: " + cipher.decrypt(ciphertexts[i]));
            i++;
        }
    }

    // Hex encoding lifted from commons-codec

    private static String hexEncode(byte [] input) {
        return new String(encodeHex(input));
    }

    private static char[] encodeHex(byte[] data) {

        int l = data.length;

           char[] out = new char[l << 1];

           // two characters form the hex value.
           for (int i = 0, j = 0; i < l; i++) {
               out[j++] = DIGITS[(0xF0 & data[i]) >>> 4 ];
               out[j++] = DIGITS[ 0x0F & data[i] ];
           }

           return out;
    }

    private static final char[] DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7',
           '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private static class StringEncrypter {

        private final SecretKey key;
        private final String algorithm;

        public StringEncrypter(SecretKey key, String algorithm) {
            this.key = key;
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public byte[] encrypt(String input) throws Exception {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return cipher.doFinal(input.getBytes("UTF-8"));
        }

        public String decrypt(byte[] input) throws Exception {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);

            return new String(cipher.doFinal(input));
        }

    }
}

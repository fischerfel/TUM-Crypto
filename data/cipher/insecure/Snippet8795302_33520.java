public class Foo {

    public static void main(String[] args) {
        try {
            String cipherSpec = "DES/ECB/NoPadding";
            Cipher cipher = Cipher.getInstance(cipherSpec);
            int blockSize = cipher.getBlockSize();

            String keyText = "happy";
            Key key = new SecretKeySpec(padRight(keyText, blockSize).getBytes("UTF-8"), "DES");

            String input = "http://google.com";
                   input = padRight(input, input.length() + blockSize - (input.length() % blockSize));

            // encrypt
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(input.getBytes(CHARSET));
            System.out.println("\ncipher text: ");
            System.out.println(new String(cipherText, CHARSET));

            // decrypt
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = cipher.doFinal(cipherText);
            System.out.println("\nplain text: ");
            System.out.println(new String(plainText, CHARSET));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final static String CHARSET = "UTF-8";

    static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }
}

public class Main {
    private String randomized = "21232d0960a7b522d3e25141e54ecee6";
    private String keySuffix = "1dad418a";
    private String cryptogram = "00110001 01111000 01111101 01111100 01100001 11011110 10010010 01011011";
    private byte[] cryptogramBytes;
    private String pattern = "[a-zA-Z1-9\\s]*";
    private IvParameterSpec ivSpec = null;
    private Cipher cipher = null;

    public static void main(String... args){
        char[] elements = { 'a', 'b', 'c', 'd', 'e', 'f', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
        char[] buff = new char[8];
        Main main = new Main ();
        byte[] convertedRandomized = DatatypeConverter.parseHexBinary(main.randomized);
        main.ivSpec = new IvParameterSpec(convertedRandomized);
        main.cryptogram = main.cryptogram.replaceAll("\\s", "");
        BigInteger bigint = new BigInteger(main.cryptogram, 2);
        main.cryptogramBytes = bigint.toByteArray();
        main.cipher = Cipher.getInstance("AES/CBC/NoPadding");
        main.permGen(elements, 0, 8, buff);
    }

    public void permGen(char[] s, int i, int k, char[] buff){
        if (i < k) {
            for (int j = 0; j < s.length; j++) {
                buff[i] = s[j];
                permGen(s, i + 1, k, buff);
            }
        } else {
            String result = decrypt(String.valueOf(buff) + keySuffix);
            if (result.matches(pattern))
                System.out.println("Key is: " + String.valueOf(buff) + keySuffix);
        }
    }

    public String decrypt(String key){
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
        return new String(cipher.doFinal(cryptogramBytes));
    }
}

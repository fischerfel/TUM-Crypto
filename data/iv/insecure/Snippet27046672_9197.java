public class Decryptor {

    private static final String IV = "3ad5485e60a4fecde36fa49ff63817dc";
    private static final String KEY = "0a948a068f5d4d8b9cc45df90b58d382d2b916c25822b6f74ea96fe6823132f4";
    private final static String CIPHERTEXT = "4E6B9EC6B5A0614BF9D69C5B0B5AE03D27484F2DB9D450E50EE623E80B8E34F5";

    public static final void main(String[] args) throws DecoderException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec sks = new SecretKeySpec(Hex.decodeHex(KEY.toCharArray()), "AES");
        IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex(IV.toCharArray()));
        Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
        c.init(Cipher.DECRYPT_MODE, sks, iv);
        byte[] plain = c.doFinal(Hex.decodeHex(CIPHERTEXT.toCharArray()));
        String plainText = new String(plain);
        System.out.println(plainText);
    }
}

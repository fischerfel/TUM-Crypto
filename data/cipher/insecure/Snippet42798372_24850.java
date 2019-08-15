public class SymmetricDESedeCipher {
    private static final String DATA = "Whackabad";
    private static final String ALGORITHM = "DESede";
    private static final String XFORM = "DESede/CBC/PKCS5Padding";
    private static final String KEY = "s9e42J3PpmQv8n5T8L3zzuFaGdrzK/wU";
    private static final String IV = "VDiJjncs4ak=";

    private static byte[] encrypt(String data,
                                  SecretKey key, String XFORM, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(XFORM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(data.getBytes());
    }

    public static void main(String[] unused) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(new BASE64Decoder().decodeBuffer(KEY));
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = secretKeyFactory.generateSecret(spec);

        byte[] encBytes = encrypt(DATA, secretKey, XFORM,  new BASE64Decoder().decodeBuffer(IV));

        System.out.println("Data: " + DATA);
        System.out.println("Encrypted Data: " + new BASE64Encoder().encode(encBytes));
    }
}

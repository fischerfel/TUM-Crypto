public class AESPaddingTest {

    private enum Mode {
        CBC, ECB, CFB, OFB, PCBC
    };

    private enum Padding {
        NoPadding, PKCS5Padding, PKCS7Padding, ISO10126d2Padding, X932Padding, ISO7816d4Padding, ZeroBytePadding
    }

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = new byte[] { 'X', 'X', 'X', 'X',
            'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X' };

    @BeforeClass
    public static void configBouncy() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void testECBPKCS5Padding() throws Exception {
        decrypt("bEpi03epVkSBTFaXlNiHhw==", Mode.ECB,
                Padding.PKCS5Padding);
    }

    private String decrypt(String valueToDec, Mode modeOption,
            Padding paddingOption) throws GeneralSecurityException {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);

        Cipher c = Cipher.getInstance(ALGORITHM + "/" + modeOption.name() + "/" + paddingOption.name());

        c.init(Cipher.DECRYPT_MODE, key);

        byte[] decValue = c.doFinal(valueToDec.getBytes());

        String clear = new String(Base64.encodeBase64(decValue));

        return clear;
    }

}

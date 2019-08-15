public class CryptoTests {

private static KeyPair keys;

@BeforeClass
public static void init() throws NoSuchAlgorithmException{
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    SecureRandom random = CryptoUtils.getSecureRandom();
    keyGen.initialize(2176, random);
    keys = keyGen.generateKeyPair();
}
@Test
public void testRepeatabilityPlainRSAPublic() throws EdrmCryptoException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
    byte[] plaintext = new byte [10];
    Random r = new Random();
    r.nextBytes(plaintext);

    Cipher rsa = Cipher.getInstance("RSA");
    rsa.init(Cipher.ENCRYPT_MODE, keys.getPublic());
    byte[] encrypted1 =  rsa.doFinal(plaintext);

    rsa = Cipher.getInstance("RSA");
    rsa.init(Cipher.ENCRYPT_MODE, keys.getPublic());
    byte[] encrypted2 =  rsa.doFinal(plaintext);

    rsa = Cipher.getInstance("RSA");
    rsa.init(Cipher.ENCRYPT_MODE, keys.getPublic());
    byte[] encrypted3 =  rsa.doFinal(plaintext);

    assertArrayEquals(encrypted1, encrypted2);
    assertArrayEquals(encrypted1, encrypted3);
}
}

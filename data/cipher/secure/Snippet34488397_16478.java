@Test
public void test_cipher() {
    private static final String PROVIDER_NAME = "BC";
    final Provider provider = Security.getProvider(PROVIDER_NAME);

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
}

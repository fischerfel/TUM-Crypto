private static final String ALGORITHM = "AES"; 

public String encrypt(final String valueEnc, final String secKey) { 

    String encryptedVal = null;

    try {
        final Key key = generateKeyFromString(secKey);
        final Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        final byte[] encValue = c.doFinal(valueEnc.getBytes());
        encryptedVal = new BASE64Encoder().encode(encValue);
    } catch(Exception ex) {
        System.out.println("The Exception is=" + ex);
    }

    return encryptedVal;
}

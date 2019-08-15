import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public final void testTesting() throws Exception {
    final String plainText = "plain text";
    final String result = encrypt(plainText);
    System.out.println(result);
}

public String encrypt(final String plainText) throws Exception {
    final byte[] data = plainText.getBytes("UTF-8");
    final byte[] encoded = encrypt(data);
    final String result = new String(encoded);
    return result;
}

public byte[] encrypt(final byte[] data) throws Exception {
    // this is just an example key, real code should use a properly generated shared secret
    final byte[] secret = new byte[] {42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42};
    final SecretKeySpec key = new SecretKeySpec(secret, "AES");

    final Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    encryptCipher.init(Cipher.ENCRYPT_MODE, key);
    final byte[] iv = encryptCipher.getIV();
    final byte[] encrypted = encryptCipher.doFinal(data);

    final int outputLength = encrypted.length;
    final int ivLength = iv.length;

    final byte[] results = new byte[outputLength + ivLength];
    System.arraycopy(iv, 0, results, 0, ivLength);
    System.arraycopy(encrypted, 0, results, ivLength, outputLength);

    return DatatypeConverter.printBase64Binary(encoded);
}

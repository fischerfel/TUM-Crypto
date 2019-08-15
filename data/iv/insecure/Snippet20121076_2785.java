    public class TripleDesTest {

private KeySpec keySpec;
private SecretKey key;
private IvParameterSpec iv;

public TripleDesTest() {
    String keyString = "THE_KEY";
    String ivString = "THE_IV";

    try {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest(Base64.decodeBase64(keyString.getBytes("UTF-8")));            
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        keySpec = new DESedeKeySpec(keyBytes);

        key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);

        iv = new IvParameterSpec(ivString.getBytes("UTF-8"));
    } catch (Exception e) {
        e.printStackTrace();
    }

}


public String decrypt(String value) {

    try {
        Cipher dcipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "SunJCE");
        dcipher.init(Cipher.DECRYPT_MODE, key, iv);

        if (value == null)
            return null;

        // Decode base64 to get bytes
        byte[] dec = Base64.decodeBase64(value.getBytes("UTF-8"));

        // Decrypt
        byte[] utf8 = dcipher.doFinal(dec);

        // Decode using UTF-8
        return new String(utf8, "UTF-8");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;

}
    }

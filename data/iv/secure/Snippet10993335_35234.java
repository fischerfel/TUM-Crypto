public class CryptData {
private KeySpec keySpec;
private SecretKey key;
private IvParameterSpec iv;

public CryptData(String keyString, String ivString) {
    try {
        final MessageDigest md = MessageDigest.getInstance("md5");

        final byte[] digestOfPassword = md.digest(Base64
                .decodeBase64(keyString.getBytes("ISO-8859-1")));

        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        //keySpec = new DESedeKeySpec(keyBytes);
        keySpec = new DESedeKeySpec(keyString.getBytes());

        key = SecretKeyFactory.getInstance("DESede")
                .generateSecret(keySpec);

        iv = new IvParameterSpec(ivString.getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public String encrypt(String value) {
    try {
        Cipher ecipher = Cipher.getInstance("DESede/CFB/NoPadding");

                //"SunJCE");
        ecipher.init(Cipher.ENCRYPT_MODE, key, iv);

        if (value == null)
            return null;

        // Encode the string into bytes using utf-8
        byte[] valeur = value.getBytes("ISO-8859-1");
        //byte[] utf8 = value.getBytes();

        // Encrypt
        byte[] enc = ecipher.doFinal(valeur);

        // Encode bytes to base64 to get a string
        return new String(Base64.encodeBase64(enc), "ISO-8859-1");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

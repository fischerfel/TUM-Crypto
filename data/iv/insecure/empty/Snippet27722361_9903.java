public class Obfuscator {
    private final String _P = "(PUT A RANDOM 16-CHAR STRING HERE)";

    public String obfuscate(String value) throws GeneralSecurityException {
        byte[] raw = _P.getBytes(Charset.forName("US-ASCII"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
        byte[] bytes = cipher.doFinal(value.getBytes(Charset.forName("US-ASCII")));
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public String deobfuscate(String encrypted) throws GeneralSecurityException {
        byte[] bytes = Base64.decode(encrypted, 0);
        byte[] raw = _P.getBytes(Charset.forName("US-ASCII"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,
                new IvParameterSpec(new byte[16]));
        byte[] original = cipher.doFinal(bytes);
        return new String(original, Charset.forName("US-ASCII"));
    }
}

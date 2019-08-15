public class CipherUtils {
    public static byte[] getKey(String password, byte[] salt) {
        try {
            byte[] passwordSalt = EncodingUtils.getAsciiBytes(password);
            passwordSalt = concatenateByteArrays(passwordSalt, salt);

            byte[] hash1 = getHashForHash(null, passwordSalt);
            byte[] hash2 = getHashForHash(hash1, passwordSalt);
            byte[] key = concatenateByteArrays(hash1, hash2);

            return key;
        } catch (Exception e) {
            return null;
        }

    }

    public static byte[] getIV(String password, byte[] salt) {
        try {
            byte[] passwordSalt = EncodingUtils.getAsciiBytes(password);
            passwordSalt = concatenateByteArrays(passwordSalt, salt);
            byte[] hash1 = getHashForHash(null, passwordSalt);
            byte[] hash2 = getHashForHash(hash1, passwordSalt);
            byte[] hash3 = getHashForHash(hash2, passwordSalt);
            return hash3;
        } catch (Exception e) {
            return null;
        }

    }

    private static byte[] getHashForHash(byte[] hash, byte[] passwordSalt) {
        try {
            byte[] hashMaterial = concatenateByteArrays(hash, passwordSalt);
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(hashMaterial);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}

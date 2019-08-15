/**
 * hashes:
 * e7cfa2be5969e235138356a54bad7fc4
 * 3c9ec110aa171b57bb41fc761130822c
 *
 * compiled with java 8 - 12 Dec 2015
 */
public static String generateHash() {
    long r = new java.util.Random().nextLong();
    String input = String.valueOf(r);
    String md5 = null;

    try {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        //Update input string in message digest
        digest.update(input.getBytes(), 0, input.length());
        //Converts message digest value in base 16 (hex)
        md5 = new java.math.BigInteger(1, digest.digest()).toString(16);
    }
    catch (java.security.NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return md5;
}

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

....

String username = ... // from UI input
String plaintext_password = ... // from UI input

String pwhash_from_passwd = makePwHash(username, plaintext_password);

String pwhash_from_db = ...   // SELECT pwhash FROM person WHERE userName=?

if (pwhash_from_db.equals(pw_hash_from_passwd)) {
    // user is authenticated
} else {
    // invalid username or password
}

...



protected static String makePwHash(String username, String plaintext_password) {
    MessageDigest mdigest=null;
    try {
        mdigest = MessageDigest.getInstance("MD5");
        String dbs = username + plaintext_password;
        byte mdbytes[] = mdigest.digest(dbs.getBytes());
        return toHexString(mdbytes);
    } catch (NoSuchAlgorithmException e) { }
    return null;
}



private static final char[] toHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

/**
 * convert an array of bytes to an hexadecimal string
 * @return a string (length = 2 * b.length)
 * @param b bytes array to convert to a hexadecimal string
 */
public static String toHexString(byte b[]) {
    int pos = 0;
    char[] c = new char[b.length*2];
    for (int i=0; i< b.length; i++) {
        c[pos++] = toHex[(b[i] >> 4) & 0x0F];
        c[pos++] = toHex[b[i] & 0x0f];
    }
    return new String(c);
}

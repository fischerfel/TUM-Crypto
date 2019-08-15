public final class MD5 {
public enum SaltOption {
    BEFORE, AFTER, BOTH, NONE;
}
private static final String ALG = "MD5";
//For conversion to 2-char hex
private static final char[] digits = {
    '0' , '1' , '2' , '3' , '4' , '5' ,
    '6' , '7' , '8' , '9' , 'a' , 'b' ,
    'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
    'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
    'o' , 'p' , 'q' , 'r' , 's' , 't' ,
    'u' , 'v' , 'w' , 'x' , 'y' , 'z'
};

private SaltOption opt;

/**
 * Added the SaltOption constructor since everybody
 * has their own standards when it comes to salting
 * hashes.
 * 
 * This gives the developer the option...
 * 
 * @param option The salt option to use, BEFORE, AFTER, BOTH or NONE.
 */
public MD5(final SaltOption option) {
    //TODO: Add Char Encoding options too... I was too lazy!
    this.opt = option;
}

/**
 * 
 * Returns the salted MD5 checksum of the text passed in as an argument.
 * 
 * If the salt is an empty byte array - no salt is applied.
 * 
 * @param txt The text to run through the MD5 algorithm.
 * @param salt The salt value in bytes.
 * @return The salted MD5 checksum as a <code>byte[]</code>
 * @throws NoSuchAlgorithmException
 */
private byte[] createChecksum(final String txt, final byte[] salt) throws NoSuchAlgorithmException {
    final MessageDigest complete = MessageDigest.getInstance(ALG);
    if(opt.equals(SaltOption.BEFORE) || opt.equals(SaltOption.BOTH)) {
        complete.update(salt);
    }
    complete.update(txt.getBytes());
    if(opt.equals(SaltOption.AFTER) || opt.equals(SaltOption.BOTH)) {
        complete.update(salt);
    }
    return complete.digest();
}

/**
 * 
 * Returns the salted MD5 checksum of the file passed in as an argument.
 * 
 * If the salt is an empty byte array - no salt is applied.
 * 
 * @param fle The file to run through the MD5 algorithm.
 * @param salt The salt value in bytes.
 * @return The salted MD5 checksum as a <code>byte[]</code>
 * @throws IOException
 * @throws NoSuchAlgorithmException
 */
private byte[] createChecksum(final File fle, final byte[] salt)
        throws IOException, NoSuchAlgorithmException {
    final byte[] buffer = new byte[1024];
    final MessageDigest complete = MessageDigest.getInstance(ALG);
            if(opt.equals(SaltOption.BEFORE) || opt.equals(SaltOption.BOTH)) {
            complete.update(salt);
        }
    int numRead;
    InputStream fis = null;
    try {
        fis = new FileInputStream(fle);
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
    } finally {
    if (fis != null) {
            fis.close();
        }
    }
            if(opt.equals(SaltOption.AFTER) || opt.equals(SaltOption.BOTH)) {
            complete.update(salt);
        }
    return complete.digest();
}

/**
 * 
 * Efficiently converts a byte array to its 2 char per byte hex equivalent.
 * 
 * This was adapted from JDK code in the Integer class, I just didn't like
 * having to use substrings once I got the result...
 *
 * @param b The byte array to convert
 * @return The converted String, 2 chars per byte...
 */
private String convertToHex(final byte[] b) {
    int x;
    int charPos;
    int radix;
    int mask;
    final char[] buf = new char[32];
    final char[] tmp = new char[3];
    final StringBuilder md5 = new StringBuilder();
    for (int i = 0; i < b.length; i++) {
        x = (b[i] & 0xFF) | 0x100;
        charPos = 32;
        radix = 1 << 4;
        mask = radix - 1;
        do {
            buf[--charPos] = digits[x & mask];
            x >>>= 4;
        } while (x != 0);
        System.arraycopy(buf, charPos, tmp, 0, (32 - charPos));
        md5.append(Arrays.copyOfRange(tmp, 1, 3));
    }
    return md5.toString();
}

/**
 * 
 * Returns the salted MD5 checksum of the file passed in as an argument.
 * 
 * @param fle The file you want want to run through the MD5 algorithm.
 * @param salt The salt value in bytes
 * @return The salted MD5 checksum as a 2 char per byte HEX <code>String</code>
 * @throws NoSuchAlgorithmException
 * @throws IOException
 */
public String getMD5Checksum(final File fle, final byte[] salt)
        throws NoSuchAlgorithmException, IOException {
    return convertToHex(createChecksum(fle, salt));
}

/**
 * 
 * Returns the MD5 checksum of the file passed in as an argument.
 * 
 * @param fle The file you want want to run through the MD5 algorithm.
 * @return The MD5 checksum as a 2 char per byte HEX <code>String</code>
 * @throws NoSuchAlgorithmException
 * @throws IOException
 */
public String getMD5Checksum(final File fle)
        throws NoSuchAlgorithmException, IOException {
    return convertToHex(createChecksum(fle, new byte[0]));
}

/**
 * 
 * Returns the salted MD5 checksum of the text passed in as an argument.
 * 
 * @param txt The text you want want to run through the MD5 algorithm.
 * @param salt The salt value in bytes.
 * @return The salted MD5 checksum as a 2 char per byte HEX <code>String</code>
 * @throws NoSuchAlgorithmException
 * @throws IOException
 */
public String getMD5Checksum(final String txt, final byte[] salt)
        throws NoSuchAlgorithmException {
    return convertToHex(createChecksum(txt, salt));
}

/**
 * 
 * Returns the MD5 checksum of the text passed in as an argument.
 * 
 * @param txt The text you want want to run through the MD5 algorithm.
 * @return The MD5 checksum as a 2 char per byte HEX <code>String</code>
 * @throws NoSuchAlgorithmException
 * @throws IOException
 */
public String getMD5Checksum(final String txt)
        throws NoSuchAlgorithmException {

    return convertToHex(createChecksum(txt, new byte[0]));
}
}

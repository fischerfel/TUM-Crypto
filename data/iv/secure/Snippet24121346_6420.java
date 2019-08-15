class Crypto {


String mPassword = null;
public final static int SALT_LEN = 8;
byte[] mInitVec = null;
byte[] mSalt = null;
Cipher mEcipher = null;
Cipher mDecipher = null;
private final int KEYLEN_BITS = 128; // see notes below where this is used.
private final int ITERATIONS = 65536;
private final int MAX_FILE_BUF = 1024;

public Crypto(String password) {mPassword = password;}
public byte[] getSalt() {return (mSalt);}
public byte[] getInitVec() {return (mInitVec);}

public void setupEncrypt() throws Exception {
    mSalt = new byte[SALT_LEN];
    SecureRandom rnd = new SecureRandom();
    rnd.nextBytes(mSalt);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(mPassword.toCharArray(), mSalt, ITERATIONS, KEYLEN_BITS);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    mEcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    mEcipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = mEcipher.getParameters();
    mInitVec = params.getParameterSpec(IvParameterSpec.class).getIV();
}

public void setupDecrypt(String initvec, String salt) throws Exception {
    mSalt = decodeHex(salt.toCharArray());
    mInitVec = decodeHex(initvec.toCharArray());
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(mPassword.toCharArray(), mSalt, ITERATIONS, KEYLEN_BITS);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    mDecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    mDecipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(mInitVec));
}

public byte[] decrypt(byte[] data) throws IllegalBlockSizeException, BadPaddingException, IOException {
    byte[] decdata = new byte[data.length];
    int totalread = 0;
    int nread = 0;
    byte[] substr = new byte[16];
    InputStream fin = new ByteArrayInputStream(data);
    CipherInputStream cin = new CipherInputStream(fin, mDecipher);
    while ((nread = cin.read(substr)) > 0) {
        for (int i = 0; i < nread; i++) decdata[totalread+i] = substr[i];
        totalread += nread;
    }
    fin.close();
    return decdata;
}

public byte[] encrypt(byte[] data) throws IllegalBlockSizeException, BadPaddingException, IOException {
    System.out.println("data.length="+data.length);
    byte[] encdata = new byte[data.length+15-(data.length-1)%16];
    System.out.println("encdata.length="+encdata.length);
    int totalread = 0;
    int nread = 0;
    byte[] substr = new byte[16];
    InputStream fin = new ByteArrayInputStream(data);
    CipherInputStream cin = new CipherInputStream(fin, mEcipher);
    while ((nread = cin.read(substr)) > 0 && totalread<data.length) {
        for (int i = 0; i < nread; i++) encdata[totalread+i] = substr[i];
        totalread += nread;
    }
    fin.close();
    return encdata;
}

public static void main(String[] args) throws Exception {
    String inpstr = "Dit is een test.Zit if een mewt.";

    Crypto en = new Crypto("mypassword");
    en.setupEncrypt();
    String iv = encodeHexString(en.getInitVec()).toUpperCase();
    String salt = encodeHexString(en.getSalt()).toUpperCase();
    byte[] inp = inpstr.getBytes();
    byte[] enc = en.encrypt(inp);
    System.out.println("In: "+Arrays.toString(inp));
    System.out.println("En: "+Arrays.toString(enc));

    Crypto dc = new Crypto("mypassword");
    dc.setupDecrypt(iv, salt);
    byte[] oup = dc.decrypt(enc);
    System.out.println("En: "+Arrays.toString(enc));
    System.out.println("Ou: "+Arrays.toString(oup));
}

public static final String DEFAULT_CHARSET_NAME = "UTF_8";
private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

private static byte[] decodeHex(char[] data) {
    int len = data.length;
    if ((len & 0x01) != 0) {
        throw new UnsupportedOperationException("Odd number of characters.");
    }
    byte[] out = new byte[len >> 1];

    // two characters form the hex value.
    for (int i = 0, j = 0; j < len; i++) {
        int f = toDigit(data[j], j) << 4;
        j++;
        f = f | toDigit(data[j], j);
        j++;
        out[i] = (byte) (f & 0xFF);
    }

    return out;
}

private static char[] encodeHex(byte[] data) {
    return encodeHex(data, true);
}

private static char[] encodeHex(byte[] data, boolean toLowerCase) {
    return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
}

private static char[] encodeHex(byte[] data, char[] toDigits) {
    int l = data.length;
    char[] out = new char[l << 1];
    // two characters form the hex value.
    for (int i = 0, j = 0; i < l; i++) {
        out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
        out[j++] = toDigits[0x0F & data[i]];
    }
    return out;
}

private static String encodeHexString(byte[] data) {
    return new String(encodeHex(data));
}

private static int toDigit(char ch, int index) {
    int digit = Character.digit(ch, 16);
    if (digit == -1) {
        throw new UnsupportedOperationException("Illegal hexadecimal character " + ch + " at index " + index);
    }
    return digit;
}

}

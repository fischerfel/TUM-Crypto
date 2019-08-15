// password to be crypted
public String pass = "password_go_here";

// key for encrypt pass
public String passKey = generateRandomKey(); // generation clef 16 caractere [a-zA-Z0-9]

// Encrypted pass
public String cryptedPass;

public final Logger logger = Logger.getLogger(this.getClass());

private final static byte[] IV_BYTES = new byte[] { 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01,
        0x00, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00 };

public Crypto() {
    super();
}

public void encryptPass(){

    Security.addProvider(new BouncyCastleProvider());
    IvParameterSpec _ivSpec = new IvParameterSpec(IV_BYTES);

    try{
        KeyGenerator _keygen = KeyGenerator.getInstance("AES");
        _keygen.init(new SecureRandom(passKey.getBytes()));
        SecretKey _key = _keygen.generateKey();
        logger.trace("Secret key generated");

        Cipher _cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        _cipher.init(Cipher.ENCRYPT_MODE, _key, _ivSpec);

        cryptedPass = asHex(_cipher.doFinal(pass.getBytes("UTF-8")));


        logger.trace("Encrypted pass : "+cryptedPass);


    }catch (Exception e) {
        logger.warn("encrypt failed");
        e.printStackTrace();
    }
}

public void decryptPass() {
    byte[] _passKey = passKey.getBytes();
    byte[] _cryptedPass = hexFromString(cryptedPass);

    Security.addProvider(new BouncyCastleProvider());   
    IvParameterSpec _ivSpec = new IvParameterSpec(IV_BYTES);

    try {           
        KeyGenerator _keygen = KeyGenerator.getInstance("AES");
        _keygen.init(new SecureRandom(_passKey));
        SecretKey _key = _keygen.generateKey();
        logger.trace("Secret key generated");

        Cipher _cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        _cipher.init(Cipher.DECRYPT_MODE, _key, _ivSpec);

        String _pass = new String(_cipher.doFinal(_cryptedPass), "UTF-8");

        logger.trace("Decrypted pass : "+_pass);

    } catch (Exception e) {
        logger.warn("decrypt failed");
        e.printStackTrace();
    }

}

private int fromDigit(char ch) {
    if ((ch >= '0') && (ch <= '9')) {
        return ch - '0';
    } else if ((ch >= 'A') && (ch <= 'F')) {
        return ch + 10 - 'A';
    } else if ((ch >= 'a') && (ch <= 'f')) {
        return ch + 10 - 'a';
    } else {
        throw new IllegalArgumentException(String.format(
                "Invalid hex character 0x%04x", 0xff & ch));
    }
}

private byte[] hexFromString(String hex) {
    final byte[] buf = new byte[hex.length() / 2];
    for (int i = 0, j = 0; i < hex.length(); i += 2) {
        buf[j++] = (byte) (fromDigit(hex.charAt(i)) << 4 | fromDigit(hex
                .charAt(i + 1)));
    }
    return buf;
}

private static String asHex(byte buf[]) {
    final Formatter formatter = new Formatter(new StringBuffer());
    for (int i = 0; i < buf.length; i++) {
        formatter.format("%02x", 0xff & buf[i]);
    }
    return formatter.toString();
}

private String generateRandomKey() {
    String _chars = "abcdefABCDEF1234567890";
    StringBuffer _pass = new StringBuffer();
    for (int x = 0; x < 32; x++) {
        int i = (int) Math.floor(Math.random() * (_chars.length() - 1));
        _pass.append(_chars.charAt(i));
    }
    return _pass.toString();
}

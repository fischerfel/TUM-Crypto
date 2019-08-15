public class Oracle1 {

private static Logger logger = LoggerFactory.getLogger(Oracle1.class);

final private byte[] SuffixBytes;

private Cipher cipher;
private static String ALGO = Config.ALGO;
private static int BlockSize = getAlgoBlockSize(ALGO);

public Oracle1() {
    this(Config.p1Key, Config.p1Suffix);
}

private void init(byte[] keyBytes) {
    Security.addProvider(new BouncyCastleProvider());
    SecretKey KEY;
    try {
        if (isSizeLegal(keyBytes, ALGO)) {
            KEY = new SecretKeySpec(keyBytes, ALGO);
        } else {
            throw new RuntimeException(String.format("illegal ALGO %s with key length %d", ALGO, keyBytes.length));
        }
        cipher = Cipher.getInstance(String.format("%s/ECB/NoPadding", ALGO));
        cipher.init(Cipher.ENCRYPT_MODE, KEY);
    } catch (GeneralSecurityException e) {
        e.printStackTrace();
        System.exit(1);
    }
}

public Oracle1(byte[] keyBytes, byte[] suffixBytes) {
    SuffixBytes = suffixBytes;
    init(keyBytes);
}

public Oracle1(String keyString, String suffixString) {
    byte[] keyBytes = keyString.getBytes();
    SuffixBytes = suffixString.getBytes();
    init(keyBytes);
}

public byte[] compose(String plainText) {
    return compose(plainText.getBytes());
}

public byte[] compose(byte[] bytes) {
    int byteLength = bytes.length;
    if (byteLength > BlockSize) {
        logger.info("input length {} > {}", byteLength, BlockSize);
    }

    byte[] inputBytes = concat(bytes, SuffixBytes);
    byte[] finalBytes = paddingBytes(inputBytes, BlockSize);
    byte[] cipherBytes = new byte[0];
    try {
        cipherBytes = cipher.doFinal(finalBytes);
        logger.info("input:{}\tcipher:{}", finalBytes, cipherBytes);
    } catch (GeneralSecurityException e) {
        e.printStackTrace();
        System.exit(1);
    }
    return cipherBytes;
}

}

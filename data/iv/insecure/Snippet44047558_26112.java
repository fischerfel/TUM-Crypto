public class DECODECLASS {
public static final int CHUNK_SIZE = 6144;
public static final String PKCS7_PADDING = "AES/CBC/PKCS7Padding";
public static final int STARTING_LOCATION = 0;
public static final int STREAM_FINISH_LOCATION = -1;

public void encrypt(File inputFile, File outputFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
    FileInputStream fis = new FileInputStream(inputFile);
    FileOutputStream fos = new FileOutputStream(outputFile);
    Cipher cipher = Cipher.getInstance(PKCS7_PADDING);
    byte[] keyBytes = new byte[32];
    System.arraycopy(MessageDigest.getInstance("SHA-256").digest(), STARTING_LOCATION, keyBytes, STARTING_LOCATION, keyBytes.length);
    cipher.init(1, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(new byte[16]));
    CipherInputStream inputStream = new CipherInputStream(fis, cipher);
    byte[] data = new byte[CHUNK_SIZE];
    while (true) {
        int count = inputStream.read(data, STARTING_LOCATION, CHUNK_SIZE);
        if (count != STREAM_FINISH_LOCATION) {
            fos.write(data, STARTING_LOCATION, count);
        } else {
            fis.close();
            fos.close();
            inputStream.close();
            return;
        }
    }
}

public void decrypt(File inputFile, File outputfile) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
    FileInputStream fis = new FileInputStream(inputFile);
    FileOutputStream fos = new FileOutputStream(outputfile);
    Cipher cipher = Cipher.getInstance(PKCS7_PADDING);
    byte[] keyBytes = new byte[32];
    System.arraycopy(MessageDigest.getInstance("SHA-256").digest(), STARTING_LOCATION, keyBytes, STARTING_LOCATION, keyBytes.length);
    cipher.init(2, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(new byte[16]));
    CipherInputStream inputStream = new CipherInputStream(fis, cipher);
    byte[] data = new byte[CHUNK_SIZE];
    while (true) {
        int count = inputStream.read(data, STARTING_LOCATION, CHUNK_SIZE);
        if (count != STREAM_FINISH_LOCATION) {
            fos.write(data, STARTING_LOCATION, count);
        } else {
            fis.close();
            fos.close();
            inputStream.close();
            return;
        }
    }
} }

public class DataEncryptDecrypt {
public Cipher encryptcipher, decryptCipher;
int blockSize = 16;
String TAG = "DataEncryptDecrypt";
private static final String RANDOM_ALGORITHM = "SHA1PRNG";

public DataEncryptDecrypt(String passwd) {
    final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";
    //AES/CBC/PKCS7Padding
    char[] humanPassphrase = passwd.toCharArray();
    byte[] salt = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0xE,
            0xF }; // must save this!
    //salt =generateSalt();
    final int HASH_ITERATIONS = 100;
    final int KEY_LENGTH = 128; //256
    PBEKeySpec mykeyspec = new PBEKeySpec(humanPassphrase, salt,
            HASH_ITERATIONS, KEY_LENGTH);
//  final String KEY_GENERATION_ALG = "PBEWITHSHAANDTWOFISH-CBC";
    final String KEY_GENERATION_ALG="PBEWithMD5And128BitAES-CBC-OpenSSL";
    SecretKey sk;
    try {
        encryptcipher = Cipher.getInstance(CIPHERMODEPADDING);
        SecretKeyFactory keyfactory = SecretKeyFactory
                .getInstance(KEY_GENERATION_ALG);
        sk = keyfactory.generateSecret(mykeyspec);

        // step 1 - get an instance of the cipher, specifying the mode and
        // padding

        byte[] iv = { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC,
                0xD, 91 }; // must save this
        //iv= generateIv();
        IvParameterSpec IV = new IvParameterSpec(iv);
        encryptcipher = Cipher.getInstance(CIPHERMODEPADDING);
        decryptCipher = Cipher.getInstance(CIPHERMODEPADDING);

        // step 2 - initialize the cipher
        encryptcipher.init(Cipher.ENCRYPT_MODE, sk, IV);
        decryptCipher.init(Cipher.DECRYPT_MODE, sk, IV);

    } catch (NoSuchAlgorithmException nsae) {
        Log.e("AESdemo",
                "no key factory support for PBEWITHSHAANDTWOFISH-CBC");
    } catch (InvalidKeySpecException ikse) {
        Log.e("AESdemo", "invalid key spec for PBKDF2");
    } catch (Exception ex) {

    }
}

public String encryptData(String inputFileName) {
    String outFilename = null;
    File inputFile = new File(inputFileName);
    try {

        // step 3 - not needed, as we have all the blocks on hand

        // step 4 - call doFinal()

         outFilename = ".".concat(CommonUtils.getHash(inputFile.getName()));
        InputStream fis;
        OutputStream fos;
        fis = new BufferedInputStream(new FileInputStream(inputFileName));

        fos = new BufferedOutputStream(new FileOutputStream(
                inputFile.getParent() + "/" + outFilename));
        Log.i(TAG, "Output path:" + inputFile.getParent() + "/" + outFilename);
        byte[] buffer = new byte[blockSize];
        int noBytes = 0;
        byte[] cipherBlock = new byte[encryptcipher
                .getOutputSize(buffer.length)];
        int cipherBytes;
        while ((noBytes = fis.read(buffer)) != -1) {
            cipherBytes = encryptcipher.update(buffer, 0, noBytes,
                    cipherBlock);
            fos.write(cipherBlock, 0, cipherBytes);
        }
        // always call doFinal
        cipherBytes = encryptcipher.doFinal(cipherBlock, 0);
        fos.write(cipherBlock, 0, cipherBytes);

        // close the files
        fos.close();
        fis.close();
        Log.i("encrpty", "done");
        inputFile.delete();
    }

    catch (Exception ex) {
        ex.printStackTrace();
    }
    return inputFile.getParent() + "/" + outFilename;
}

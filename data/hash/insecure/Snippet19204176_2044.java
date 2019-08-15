public class secureFile 
{
private static SecretKeySpec sec_key_spec;
private static Cipher sec_cipher;
private static final int SIZE_LIMIT = 1024;
private static final int SHA1_SIZE = 20;


// encryption function
public static byte[] encrypt(byte[] plaintext) throws Exception
{
    byte[] encrypted = null;
    byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    try {
        //set cipher object to encrypt mode
        sec_cipher.init(Cipher.ENCRYPT_MODE, sec_key_spec, ivspec);

        //create ciphertext
        encrypted = sec_cipher.doFinal(plaintext);
    }
    catch(Exception e) {
        System.out.println(e);
    }
    return encrypted;
}


//creates SHA-1 message digest
public static byte[] createDigest(byte[] message) throws Exception
{
    byte[] hash = null;
    try{
        //create message digest object
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");

        //make message digest
        hash = sha1.digest(message);    
    }
    catch(NoSuchAlgorithmException nsae) {
        System.out.println(nsae);
    }
    return hash;
}


public static void main (String args[]) throws Exception 
{   
    byte[] key;
    byte[] key128;
    FileInputStream mFile;
    FileOutputStream cFile;
    byte[] ciphertext;
    byte[] plaintext;
    byte[] sha1_hash;   
    byte[] message;
    String cFilename;
    int file_size;

    try {
        //gets filenames and fileinput
        String[] mFilename = args[0].split("\\.(?=[^\\.]+$)");
        cFilename = mFilename[0] + "EN." + mFilename[1];
        mFile = new FileInputStream(mFilename[0] + "." + mFilename[1]);

        //limits buffer size to 1024
        file_size = mFile.available();
        if (file_size > SIZE_LIMIT) {
            file_size = SIZE_LIMIT;
        }

        //reads message file in buffer
        message = new byte[file_size];
        mFile.read(message);
        mFile.close();

        //creates sha1 digest from message
        sha1_hash = createDigest(message);

        //combines message and digest
        plaintext = new byte[file_size + SHA1_SIZE];
        for (int i = 0; i < file_size; i++) {
            plaintext[i] = message[i];
        }
        for (int j = 0; j < SHA1_SIZE; j++) {
            plaintext[file_size + j] = sha1_hash[j];
        }

        //generates key
        key = createDigest(args[1].getBytes("us-ascii"));
        key128 = Arrays.copyOfRange(key, 0, 16);
        sec_key_spec = new SecretKeySpec(key128, "AES");

        //enciphers the plaintext
        sec_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ciphertext = encrypt(plaintext);

        //copies ciphertext to file
        cFile = new FileOutputStream(cFilename);
        cFile.write(ciphertext);
        cFile.close();
    }
    catch (Exception e) {
        System.out.println(e);
    }
  }

}

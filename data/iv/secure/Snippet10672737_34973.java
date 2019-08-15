public class BouncyCastleProvider_AES_CBC {
    public Cipher encryptcipher, decryptCipher;
    String TAG = "DataEncryptDecrypt";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";

     // The default block size
    public static int blockSize = 16;


    // Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[blockSize];       //input buffer
    byte[] obuf = new byte[512];            //output buffer

    // The key
    byte[] key = null;
    // The initialization vector needed by the CBC mode
    byte[] IV = null;

    public BouncyCastleProvider_AES_CBC(String passwd){
        //for a 192 key you must install the unrestricted policy files
        //  from the JCE/JDK downloads page
        key =passwd.getBytes();
        key = "SECRETSECRET_1SE".getBytes();
        Log.i( "SECRETSECRET_1SECRET_2", "length"+ key.length);
        //default IV value initialized with 0
        IV = new byte[blockSize];
        InitCiphers();

    }

    public BouncyCastleProvider_AES_CBC(String pass, byte[] iv){
        //get the key and the IV

        IV = new byte[blockSize];
        System.arraycopy(iv, 0 , IV, 0, iv.length);
    }
    public BouncyCastleProvider_AES_CBC(byte[] pass, byte[]iv){
        //get the key and the IV
        key = new byte[pass.length];
        System.arraycopy(pass, 0 , key, 0, pass.length);
        IV = new byte[blockSize];
        System.arraycopy(iv, 0 , IV, 0, iv.length);
    }

    public void InitCiphers()
            {
        try {
       //1. create the cipher using Bouncy Castle Provider
       encryptcipher =
               Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
       //2. create the key
       SecretKey keyValue = new SecretKeySpec(key,"AES");
       //3. create the IV
       AlgorithmParameterSpec IVspec = new IvParameterSpec(IV);
       //4. init the cipher
       encryptcipher.init(Cipher.ENCRYPT_MODE, keyValue, IVspec);
       encryptcipher.getOutputSize(100);

       //1 create the cipher
       decryptCipher =
               Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
       //2. the key is already created
       //3. the IV is already created
       //4. init the cipher
       decryptCipher.init(Cipher.DECRYPT_MODE, keyValue, IVspec);
        }
        catch(Exception ex) {
            ex.printStackTrace();
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
            int bufferLength = (inputFile.length()>10000000?10000000:1000);
            byte[] buffer = new byte[bufferLength];
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

    public void decryptData(String inputFileName, String outputFileName) {
        InputStream fis;
        OutputStream fos;
        try {
            fis = new BufferedInputStream(new FileInputStream(
                    inputFileName));
            fos = new BufferedOutputStream(new FileOutputStream(
                    outputFileName));
            byte[] buffer = new byte[blockSize*100];
            int noBytes = 0;
            byte[] cipherBlock = new byte[decryptCipher
                    .getOutputSize(buffer.length)];
            int cipherBytes;
            while ((noBytes = fis.read(buffer)) != -1) {
                cipherBytes = decryptCipher.update(buffer, 0, noBytes,
                        cipherBlock);
                fos.write(cipherBlock, 0, cipherBytes);
            }
            // allways call doFinal
            cipherBytes = decryptCipher.doFinal(cipherBlock, 0);
            fos.write(cipherBlock, 0, cipherBytes);

            // close the files
            fos.close();
            fis.close();
            new File(inputFileName).delete();

            Log.i("decrypt", "done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public byte[] generateSalt()  {
        byte[] salt = new byte[16];

        try {
        SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        random.nextBytes(salt);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return salt;

    }



}

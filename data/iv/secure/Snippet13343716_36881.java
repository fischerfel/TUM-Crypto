public class EncryptDecrypt {
    public Cipher encryptcipher, decryptCipher;
    String TAG = "EncryptDecrypt";
    // The default block size
    public static int blockSize = 16;

    // Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[blockSize]; // input buffer
    byte[] obuf = new byte[8192]; // output buffer

    // The key
    byte[] key = null;  

     byte[] IV = null;

    public EncryptDecrypt(String passwd) {

        key = passwd.getBytes();
        key = "SECRETSECRET_1SE".getBytes();

        // default IV value initialized with 0
        IV = new byte[blockSize];
        initCiphers();

    }

    public void initCiphers() {
        try {
            // 1. create the cipher using Bouncy Castle Provider
            encryptcipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            // 2. create the key
            SecretKey keyValue = new SecretKeySpec(key, "AES");
            // 3. create the IV
            AlgorithmParameterSpec IVspec = new IvParameterSpec(IV);
            // 4. init the cipher
            encryptcipher.init(Cipher.ENCRYPT_MODE, keyValue, IVspec);
            encryptcipher.getOutputSize(8192);

            // 1 create the cipher
            decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            // 2. the key is already created
            // 3. the IV is already created
            // 4. init the cipher
            decryptCipher.init(Cipher.DECRYPT_MODE, keyValue, IVspec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String encryptData(URL inputFileName, String outputFileName) {
        File outFilename = new File(outputFileName);
        try {
            InputStream fis;
            OutputStream fos;
            fis = new BufferedInputStream(inputFileName.openStream(), 1000);
            fos = new BufferedOutputStream(new FileOutputStream(outFilename));
            Log.i(TAG, "Output path:" + outFilename);
             int bufferLength = (inputFileName.toString().length() > 10000000
             ? 10000000
             : 1000);
            byte[] buffer = new byte[8192];
            int noBytes = 0;
            byte[] cipherBlock = new byte[encryptcipher
                    .getOutputSize(buffer.length)];

            int cipherBytes;
            long startTime = System.currentTimeMillis();
            while ((noBytes = fis.read(buffer)) != -1) {
                cipherBytes = encryptcipher.update(buffer, 0, noBytes,
                        cipherBlock);
                fos.write(cipherBlock, 0, cipherBytes);
            }
            // always call doFinal
            cipherBytes = encryptcipher.doFinal(cipherBlock, 0);
            fos.write(cipherBlock, 0, cipherBytes);
            System.out
                    .println("encryption "
                            + " took "
                            + ((System.currentTimeMillis() - startTime) / 1000.0)
                            + "s");
            // close the files
            fos.close();
            fis.close();
            Log.i("encrypt", "done");
        }

        catch (Exception ex) {
            ex.printStackTrace();
        }
        return outFilename.toString().trim();
    }

    public String decryptData(String inputFileName, String outputFileName) {

        InputStream fis;
        OutputStream fos;
        try {
            fis = new BufferedInputStream(new FileInputStream(inputFileName));
            fos = new BufferedOutputStream(new FileOutputStream(outputFileName));
            byte[] buffer = new byte[8192];
            int noBytes = 0;
            byte[] cipherBlock = new byte[decryptCipher
                    .getOutputSize(buffer.length)];
            int cipherBytes;
            long startTime = System.currentTimeMillis();
            while ((noBytes = fis.read(buffer)) != -1) {
                cipherBytes = decryptCipher.update(buffer, 0, noBytes,
                        cipherBlock);
                Log.d("BufferRead", "" + buffer);
                fos.write(cipherBlock, 0, cipherBytes);
            }
            // allways call doFinal
            cipherBytes = decryptCipher.doFinal(cipherBlock, 0);
            fos.write(cipherBlock, 0, cipherBytes);
            System.out
                    .println("decryption "
                            + " took "
                            + ((System.currentTimeMillis() - startTime) / 1000.0)
                            + "s");
            // close the files
            fos.close();
            fis.close();
            Log.i("decrypt", "done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outputFileName.toString().trim();
    }
}

This is my code

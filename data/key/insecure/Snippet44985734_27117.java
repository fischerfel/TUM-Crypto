public static final String AES_ALGORITHM = "AES";
    public static final String AES_TRANSFORMATION = "AES/CTR/NoPadding";
    private void initKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        byte[] iv = new byte[16];
        key = "abctestkeyforand".getBytes();
        secureRandom.nextBytes(iv);

        mSecretKeySpec = new SecretKeySpec(key, AES_ALGORITHM);
        mIvParameterSpec = new IvParameterSpec(iv);


    }

public void encryptFiles(View view) {
        File musicDirectory = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
                + "/Music");
        File[] audioFiles = musicDirectory.listFiles();
        if (audioFiles != null && audioFiles.length > 0) {
            for (File singleAudioFile : audioFiles) {
                inputFile = singleAudioFile;
                Log.e("Input Filename =", inputFile.getName());
                mEncryptedFile = new File(musicDirectory, "enc_" + inputFile.getName());
                Log.e("Enc Filename =", inputFile.getName());
                if (hasFile()) {
                    Log.d(getClass().getCanonicalName(),
                            "encrypted file found, no need to recreate");
                    continue;
                }
                try {
                    Cipher encryptionCipher = Cipher.getInstance(AES_TRANSFORMATION);
                    encryptionCipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec, mIvParameterSpec);
                    new EncryptFileTask(inputFile, mEncryptedFile, encryptionCipher).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

public void encryptFile(View view) {
        inputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/oliver.mp4");

        Log.e("Input Filename =", inputFile.getName());
        mEncryptedFile = new File(inputFile.getParent(), "enc_" + inputFile.getName());
        Log.e("Enc Filename =", mEncryptedFile.getName());
        if (hasFile()) {
            Log.d(getClass().getCanonicalName(),
                    "encrypted file found, no need to recreate");
            return;
        }
        try {
            Cipher encryptionCipher = Cipher.getInstance(AES_TRANSFORMATION);
            encryptionCipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec, mIvParameterSpec);
            new EncryptFileTask(inputFile, mEncryptedFile, encryptionCipher).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


public class EncryptFileTask extends AsyncTask<Void, Void, Boolean> {

    private File inputFile;
    private File mFile;
    private Cipher mCipher;

    public EncryptFileTask(File inputFile, File file, Cipher cipher) {

        this.inputFile = inputFile;
        mFile = file;
        mCipher = cipher;
    }

    private boolean downloadAndEncrypt() throws Exception {
        try {
            long startTime = System.currentTimeMillis();
            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream fileOutputStream = new FileOutputStream(mFile);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream,
                    mCipher);

            byte buffer[] = new byte[1024 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            cipherOutputStream.close();

            float totalTimeInSeconds = (System.currentTimeMillis() - startTime) / 1000f;
            Log.e("Encryption", "Completed for " + inputFile.getName() + " in " + totalTimeInSeconds
                    + " seconds");
            return true;
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }

//    connection.disconnect();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            return downloadAndEncrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aVoid) {
        Log.e(getClass().getCanonicalName(), "Encryption done");
        if (aVoid) {
            inputFile.delete();
        }
    }
}

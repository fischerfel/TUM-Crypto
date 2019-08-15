private initCipher(int mode) {
    try {
        byte[] iv;
        mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        IvParameterSpec ivParams;
        if(mode == Cipher.ENCRYPT_MODE) {
            mCipher.init(mode, generateKey());
            ivParams = mCipher.getParameters().getParameterSpec(IvParameterSpec.class);
            iv = ivParams.getIV();
            fos = getContext().openFileOutput(IV_FILE, Context.MODE_PRIVATE);
            fos.write(iv);
            fos.close();
        }
        else {
            key = (SecretKey)keyStore.getKey(KEY_NAME, null);
            File file = new File(getContext().getFilesDir()+"/"+IV_FILE);
            int fileSize = (int)file.length();
            iv = new byte[fileSize];
            FileInputStream fis = getContext().openFileInput(IV_FILE);
            fis.read(iv, 0, fileSize);
            fis.close();
            ivParams = new IvParameterSpec(iv);
            mCipher.init(mode, key, ivParams);
        }
        mCryptoObject = new FingerprintManager.CryptoObject(mCipher);
    } catch(....)
}

public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            try {
                if (opVal.contains("encrypt")) {
                    byte[] encrypted = mCipher.doFinal("Hello World".getBytes());
                    message = Base64.encodeToString(encrypted, Base64.URL_SAFE);
                   Intent intent = new Intent(mContext, sampleAcitvity.class);


                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } else  if (opVal.contains("decrypt")) {
                         byte[] data = mCipher.doFinal(Base64.decode(message, Base64.URL_SAFE));
                        Log.d(TAG, "data: " + new String(data,"UTF-8"));
                        Toast.makeText(mContext, "sucessfully decrypt"+ new String(data,"UTF-8"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(mContext, sampleAcitvity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);


  private boolean initCipher() {
        Toast.makeText(mContext, "in initCipher", Toast.LENGTH_LONG).show();
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);
          if (opVal.contains("encrypt")) {
                mCipher.init(Cipher.ENCRYPT_MODE, key);
                IV = Base64.encodeToString(mCipher.getIV(), Base64.URL_SAFE);
           } else  if (opVal.contains("decrypt")) {
            mCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Base64.decode(IV, Base64.URL_SAFE)));
            }
          return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "issue>>>>>>>>>>>"+e.toString(), Toast.LENGTH_LONG).show();
        }
        return false;
}



try {
            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "in catch mCipher"+e.toString(), Toast.LENGTH_LONG).show();

            return false;
        }

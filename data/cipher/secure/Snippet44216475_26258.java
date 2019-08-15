public boolean createKey() {
    if(mEncreaptedKey!=null)return false;
    // Generate a key to decrypt payment credentials, tokens, etc.
    // This will most likely be a registration step for the user when they are setting up your app.
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mKeyStore = null;
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

        // Set the alias of the entry in Android KeyStore where the key will appear
        // and the constrains (purposes) in the constructor of the Builder

            KeyGenParameterSpec.Builder builder=new KeyGenParameterSpec.Builder(ACCESS_TOKEN,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    // Require that the user has unlocked in the last 30 seconds
                    .setUserAuthenticationValidityDurationSeconds(AUTHENTICATION_DURATION_SECONDS)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(false);
            }
            builder.build();
            keyGenerator.init(builder.build());
             key=  keyGenerator.generateKey();
        }else{

        }

        return true;
    } catch (NoSuchAlgorithmException | NoSuchProviderException
            | InvalidAlgorithmParameterException | KeyStoreException
            | CertificateException | IOException e) {
        throw new RuntimeException("Failed to create a symmetric key", e);
    }
}


    @RequiresApi(api = Build.VERSION_CODES.M)
public boolean tryEncrypt(Context context) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mKeyStore.load(null);
        SecretKey secretKey = (SecretKey) mKeyStore.getKey(ACCESS_TOKEN, null);
            mCipher= Cipher.getInstance(TRANSFORMATION);

        // Try encrypting something, it will only work if the user authenticated within
        // the last AUTHENTICATION_DURATION_SECONDS seconds.
            mCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptionIv = mCipher.getIV();
        byte[] results = mCipher.doFinal(ACCESS_TOKEN.getBytes(CHARSET_NAME));
        String cipher1 = Base64.encodeToString(results, Base64.DEFAULT);
        mEncreaptedKey = Base64.encodeToString(cipher1.getBytes(), Base64.DEFAULT);

        // If the user has recently authenticated, you will reach here.
        // showAlreadyAuthenticated();
        return true;
    }
    } catch (UserNotAuthenticatedException e) {
        // User is not authenticated, let's authenticate with device credentials.
       // showAuthenticationScreen();
        return false;
    } catch (KeyPermanentlyInvalidatedException e) {
        // This happens if the lock screen has been disabled or reset after the key was
        // generated after the key was generated.
        Toast.makeText(context, "Keys are invalidated after created. Retry the purchase\n"
                        + e.getMessage(),
                Toast.LENGTH_LONG).show();
        return false;
    } catch (BadPaddingException | IllegalBlockSizeException | KeyStoreException |
             UnrecoverableKeyException
            | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
        throw new RuntimeException(e);
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return false;
}

public void createKey(String keyName) {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            Log.e("MainActivity", e.getMessage());
            e.printStackTrace();
        }

        KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                keyName,
                KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                .build();
        try {
            generator.initialize(spec);
        } catch (InvalidAlgorithmParameterException e) {
            Log.e("MainActivity", e.getMessage());
        }

        generator.generateKeyPair();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createKey("Key");
        createKey("Key1");

        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry("Key", null);
        PrivateKey privateKey = (PrivateKey) privateKeyEntry.getPrivateKey();

        Cipher c;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        } else {
            c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }
        c.init(Cipher.DECRYPT_MODE, privateKey);

        mCancellationSignal = new CancellationSignal();



        String ecryptedS1 = doEncription("Key1");
        KeyStore ks1 = KeyStore.getInstance("AndroidKeyStore");
        ks1.load(null);
        KeyStore.PrivateKeyEntry privateKeyEntry1 = (KeyStore.PrivateKeyEntry) ks1.getEntry("Key1", null);
        PrivateKey privateKey1 = (PrivateKey) privateKeyEntry1.getPrivateKey();

        Cipher c1;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            c1 = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        } else {
            c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }
        c1.init(Cipher.DECRYPT_MODE, privateKey1);

        String org = "";
        try {
            org = new String(c1.doFinal(Base64.decode(ecryptedS1, Base64.DEFAULT|Base64.NO_WRAP)));
            Log.d("MainActivity", "org key1" + org);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        org = null;
        try {
            org = new String(c.doFinal(Base64.decode(doEncription("Key"), Base64.DEFAULT|Base64.NO_WRAP)));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }

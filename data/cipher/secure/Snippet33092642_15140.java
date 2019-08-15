public static final String TAG = KeyStoreManager.class.getName();

static final String CIPHER_TYPE = "RSA/ECB/PKCS1Padding";
static final String CIPHER_PROVIDER = "AndroidOpenSSL";
public static final String PHRASE_ALIAS = "phrase";
public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
public static final String MIHAIL_GUTAN_X500 = "CN=some name";


public static boolean setKeyStoreString(String fileName, String strToStore, String alias, Context context) {

    try {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);

        int nBefore = keyStore.size();

        // Create the keys if necessary
        if (!keyStore.containsAlias(alias)) {

            Calendar notBefore = Calendar.getInstance();
            Calendar notAfter = Calendar.getInstance();
            notAfter.add(Calendar.YEAR, 1);

            KeyPairGenerator generator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE);

            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(alias)
                        .setKeyType(KeyProperties.KEY_ALGORITHM_RSA)
                        .setKeySize(2048)
                        .setSubject(new X500Principal(MIHAIL_GUTAN_X500))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(notBefore.getTime())
                        .setEndDate(notAfter.getTime())
                        .build();

            generator.initialize(spec);


            KeyPair keyPair = generator.generateKeyPair(); // needs to be here
            Log.v(TAG, "The keypair" + keyPair.toString());
        }
        int nAfter = keyStore.size();
        Log.v(TAG, "Before = " + nBefore + " After = " + nAfter);

        // Retrieve the keys
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
        RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
        RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

        Log.v(TAG, "private key = " + privateKey.toString());
        Log.v(TAG, "public key = " + publicKey.toString());

        // Encrypt the text
        String dataDirectory = context.getApplicationInfo().dataDir;
        String filesDirectory = context.getFilesDir().getAbsolutePath();
        String encryptedDataFilePath = filesDirectory + File.separator + fileName;

        Log.v(TAG, "strPhrase = " + strToStore);
        Log.v(TAG, "dataDirectory = " + dataDirectory);
        Log.v(TAG, "filesDirectory = " + filesDirectory);
        Log.v(TAG, "encryptedDataFilePath = " + encryptedDataFilePath);

        Cipher inCipher = Cipher.getInstance(CIPHER_TYPE, CIPHER_PROVIDER);
        inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        CipherOutputStream cipherOutputStream =
                new CipherOutputStream(
                        new FileOutputStream(encryptedDataFilePath), inCipher);
        cipherOutputStream.write(strToStore.getBytes("UTF-8"));
        cipherOutputStream.close();
        return true;
    } catch (NoSuchAlgorithmException e) {
        Log.e(TAG, Log.getStackTraceString(e));
    } catch (NoSuchProviderException e) {
        Log.e(TAG, Log.getStackTraceString(e));
    } catch (InvalidAlgorithmParameterException e) {
        Log.e(TAG, Log.getStackTraceString(e));
    } catch (KeyStoreException e) {
        Log.e(TAG, Log.getStackTraceString(e));
    } catch (CertificateException e) {
        Log.e(TAG, Log.getStackTraceString(e));
    } catch (IOException e) {
        Log.e(TAG, Log.getStackTraceString(e));
    } catch (UnrecoverableEntryException e) {
        Log.e(TAG, Log.getStackTraceString(e));
    } catch (NoSuchPaddingException e) {
        Log.e(TAG, Log.getStackTraceString(e));
    } catch (InvalidKeyException e) {
        Log.e(TAG, Log.getStackTraceString(e));
    } catch (UnsupportedOperationException e) {
        Log.e(TAG, Log.getStackTraceString(e));
    }
    return false;
}

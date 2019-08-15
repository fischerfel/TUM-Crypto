public class EncryptDecryptUtil {
private String publicKeyStoreFileName = 

GetPropValues.getPropValue(PublisherConstants.KEYSTORE_PATH);
    private String pubKeyStorePwd = "changeit";
    private static final String SHA1PRNG = "SHA1PRNG";
    private static final String pubKeyAlias="jceksaes";
    private static final String JCEKS = "JCEKS";
    private static final String AES_PADDING = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";
    private static final int CONST_16 = 16;
    private static final int CONST_0 = 0;
    private static final String KEY_STORE = "aes-keystore";
    private static final String KEY_STORE_TYPE = "jck";
    private static final Logger logger = Logger.getLogger(KafkaPublisher.class);

    public Key getKeyFromKeyStore( String keystoreVersion) {

        KeyStore keyStore = null;
        Key key = null;
        try {
            keyStore = KeyStore.getInstance(JCEKS);
            FileInputStream stream = null;
            stream = new FileInputStream(publicKeyStoreFileName+KEY_STORE+PublisherConstants.UNDERSCORE+keystoreVersion+PublisherConstants.DOT+KEY_STORE_TYPE);
            keyStore.load(stream, pubKeyStorePwd.toCharArray());
            stream.close();
            key = keyStore.getKey(pubKeyAlias, pubKeyStorePwd.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            logger.error("Error Inside getKeyFromKeyStore, Exception = " + e);
            e.printStackTrace();
        } catch (CertificateException e) {
            logger.error("Error Inside getKeyFromKeyStore, Exception = " + e);
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            logger.error("Error Inside getKeyFromKeyStore, Exception = " + e);
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error Inside getKeyFromKeyStore, Exception = " + e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("Error Inside getKeyFromKeyStore, Exception = " + e);
            e.printStackTrace();
        }
        return key;
    }

    public String encryptData(String data) {
        String keystoreVersion = GetPropValues.getPropValue(PublisherConstants.KEYSTORE_VERSION);
        SecretKey secKey = new SecretKeySpec(getKeyFromKeyStore(keystoreVersion).getEncoded(), AES);
        String base64EncodedEncryptedMsg = null;
        Cipher cipher = null;

        try { ------- Logic -------------------}
catch() { }
}
}

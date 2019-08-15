public class EncryptionServiceImpl
{

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Resource(name = "briqueAuthentificationClient")
    private BriqueAuthentificationClientImpl briqueAuthentificationClient;

    protected static final String ALGORITHM_RSA = "RSA";

    protected static final String TRANSFORMATION_RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";

    private static final Logger LOG = LoggerFactory.getLogger(EncryptionServiceImpl.class);

    public EncryptionServiceImpl() {
        LOG.info("constructeur EncryptionServiceImpl");
    }

    /**
     * @param briqueAuthentificationClient the briqueAuthentificationClient to set
     */
    public void setBriqueAuthentificationClient(final BriqueAuthentificationClientImpl briqueAuthentificationClient) {
        this.briqueAuthentificationClient = briqueAuthentificationClient;
    }

    public String encrypt(final String input) throws GeneralSecurityException {

        if (StringUtils.isNotBlank(input)) {
            final CertificateDto certificate = this.briqueAuthentificationClient.getCurrentCertificate();

            if (certificate != null) {
                return new String(this.encryptAndEncode(input.getBytes(), certificate), EncryptionServiceImpl.UTF8);
            } else {
                throw new RuntimeException("Certificate is null");
            }
        }
        return null;
    }

    protected byte[] encryptAndEncode(final byte[] input, final CertificateDto currentCertificate)
            throws GeneralSecurityException {

        // CrÃ©ation de la clÃ© publique
        final PublicKey publicKey = this.buildPublicKey(currentCertificate);

        // Chiffre
        final byte[] inputEncrypted = this.encrypte(input, publicKey);

        // Encode
        return this.encodeBase64Url(inputEncrypted);
    }

    protected PublicKey buildPublicKey(final CertificateDto currentCertificate) throws GeneralSecurityException {

        if ("RSA".equals(currentCertificate.getKeyType())) {
            return this.buildRSAPublicKey(currentCertificate);
        }
        LOG.error(String.format("Tentative de crÃ©ation d'une clÃ© publique avec un algorithme non connu [%s]",
                currentCertificate.getKeyType()));
        return null;
    }

    protected PublicKey buildRSAPublicKey(final CertificateDto currentCertificate) throws GeneralSecurityException {

        final BigInteger modulus = new BigInteger(new String(Base64.decodeBase64(currentCertificate.getModulus()),
                EncryptionServiceImpl.UTF8));
        final BigInteger publicExponent = new BigInteger(new String(Base64.decodeBase64(currentCertificate
                .getPublicExponent()), EncryptionServiceImpl.UTF8));

        try {
            return KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
        } catch (InvalidKeySpecException e) {
            throw e;
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
    }

    protected byte[] encrypte(final byte[] input, final RSAPublicKeySpec rsaPublicKeySpec)
            throws GeneralSecurityException {

        PublicKey publicKey;
        try {
            publicKey = KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(
                    new RSAPublicKeySpec(rsaPublicKeySpec.getModulus(), rsaPublicKeySpec.getPublicExponent()));
        } catch (InvalidKeySpecException e) {
            throw e;
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
        return this.encrypte(input, publicKey);
    }

    protected byte[] encrypte(final byte[] input, final PublicKey publicKey) throws GeneralSecurityException {

        try {
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION_RSA_ECB_PKCS1PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(input);
        } catch (NoSuchAlgorithmException e) {
            throw e;
        } catch (NoSuchPaddingException e) {
            throw e;
        } catch (IllegalBlockSizeException e) {
            throw e;
        } catch (BadPaddingException e) {
            throw e;
        }

    }

    protected byte[] decrypte(final byte[] input, final RSAPrivateKeySpec rsaPrivateKeySpec)
            throws GeneralSecurityException {

        final BigInteger modulus = rsaPrivateKeySpec.getModulus();
        final BigInteger privateExponent = rsaPrivateKeySpec.getPrivateExponent();

        try {
            final PrivateKey privateKey = KeyFactory.getInstance(ALGORITHM_RSA).generatePrivate(
                    new RSAPrivateKeySpec(modulus, privateExponent));

            final Cipher cipher = Cipher.getInstance(TRANSFORMATION_RSA_ECB_PKCS1PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(input);

        } catch (NoSuchAlgorithmException e) {
            throw e;
        } catch (NoSuchPaddingException e) {
            throw e;
        } catch (IllegalBlockSizeException e) {
            throw e;
        } catch (BadPaddingException e) {
            throw e;
        } catch (InvalidKeySpecException e) {
            throw e;
        } catch (InvalidKeyException e) {
            throw e;
        }

    }

    protected byte[] encodeBase64Url(final byte[] input) {
        return Base64.encodeBase64(input, false);
    }

    protected byte[] decodeBase64Url(final byte[] input) {
        return Base64.decodeBase64(input);
    }

    /**
     * Method to connect to an url
     * 
     * @param httpclient the http connection
     * @return the response GetMethod
     * @throws OAuthException in cas of connection error
     */
    private GetMethod connect(final HttpClient httpclient, final String url) {

        final GetMethod httpget = new GetMethod(url);
        try {

            httpclient.executeMethod(httpget);

        } catch (final UnknownHostException e) {
            throw new RuntimeException("Connection ERROR - Host could not be determined.", e);
        } catch (final IOException e) {
            throw new RuntimeException("Connection ERROR - Input/Output error.", e);
        }
        return httpget;
    }

}

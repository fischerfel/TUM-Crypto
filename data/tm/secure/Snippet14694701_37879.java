public class Security
{
    private static final String protocol    = "TLS";

    @SuppressWarnings("restriction")
    private static X509Certificate generateCertificate(String dn, KeyPair pair, String algorithm) throws GeneralSecurityException, IOException
    {
        PrivateKey privkey = pair.getPrivate();
        X509CertInfo info = new X509CertInfo();
        Date from = new Date();
        Date to = new Date(from.getTime() + 10l * 365 * 24 * 60 * 60 * 1000);
        CertificateValidity interval = new CertificateValidity(from, to);
        BigInteger sn = new BigInteger(64, new SecureRandom());
        X500Name owner = new X500Name(dn);

        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
        info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
        info.set(X509CertInfo.ISSUER, new CertificateIssuerName(owner));
        info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

        // Sign the cert to identify the algorithm that's used.
        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);

        // Update the algorith, and resign.
        algo = (AlgorithmId) cert.get(X509CertImpl.SIG_ALG);
        info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
        cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);
        return cert;
    }

    protected static char[] getPassword()
    {
        return "test".toCharArray();
    }

    private static void pushKeyStoreToConfig(KeyStore ks, String configKey) throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
        IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ks.store(baos, Security.getPassword());
        Configuration.set(configKey, baos.toByteArray());
    }

    private KeyManager[]    keyManagers;
    private KeyStore        keyStore;
    private KeyStore        trustStore;
    private SSLContext      sslContext;

    private TrustManager[]  trustManagers;

    public Security()
    {
        LoggerFactory.getLogger(Security.class).debug("constructing...");
    }

    public synchronized void addCertificate(String guid, Certificate certificate) throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
        IOException
    {
        this.getTrustStore().setCertificateEntry(guid, certificate);
        Security.pushKeyStoreToConfig(this.getTrustStore(), Configuration.TRUST_STORE);
        Configuration.getInstance().save();
    }

    public SslHandler createSslHandler()
    {
        SSLEngine engine = this.getSslContext().createSSLEngine();
        engine.setUseClientMode(true);
        engine.setNeedClientAuth(true);
        return new SslHandler(engine);
    }

    public synchronized KeyManager[] getKeyManagers()
    {
        if (this.keyManagers == null)
        {
            KeyManager keyManager = new X509ExtendedKeyManager()
            {
                @Override
                public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket)
                {
                    System.out.println("keymanager chooseClientAlias");
                    return Configuration.get(Configuration.GUID, String.class);
                }

                @Override
                public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine)
                {
                    System.out.println("keymanager chooseEngineClientAlias");
                    return Configuration.get(Configuration.GUID, String.class);
                }

                @Override
                public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine)
                {
                    System.out.println("keymanager chooseEngineServerAlias");
                    return Configuration.get(Configuration.GUID, String.class);
                }

                @Override
                public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket)
                {
                    System.out.println("keymanager chooseServerAlias");
                    return Configuration.get(Configuration.GUID, String.class);
                }

                @Override
                public X509Certificate[] getCertificateChain(String alias)
                {
                    System.out.println("keymanager getCertificateChain: " + alias);
                    try
                    {
                        Certificate[] certs = Security.this.getKeyStore().getCertificateChain(alias);
                        X509Certificate[] xcerts = new X509Certificate[certs.length];
                        for (int i = 0; i < certs.length; i++)
                        {
                            xcerts[i] = (X509Certificate) certs[i];
                        }
                        return xcerts;
                    }
                    catch (Exception e)
                    {
                        LoggerFactory.getLogger(Security.class).error("Error while getting security chain", e);
                        return null;
                    }
                }

                @Override
                public String[] getClientAliases(String keyType, Principal[] issuers)
                {
                    System.out.println("keymanager getClientAliases");
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public PrivateKey getPrivateKey(String alias)
                {
                    System.out.println("keymanager getPrivateKey: " + alias);
                    try
                    {
                        return (PrivateKey) Security.this.getKeyStore().getKey(alias, Security.getPassword());
                    }
                    catch (Exception e)
                    {
                        LoggerFactory.getLogger(Security.class).error("Error while getting private key", e);
                        return null;
                    }
                }

                @Override
                public String[] getServerAliases(String keyType, Principal[] issuers)
                {
                    System.out.println("keymanager getServerAliases");
                    // TODO Auto-generated method stub
                    return null;
                }
            };
            this.keyManagers = new KeyManager[] { keyManager };
        }
        return this.keyManagers;
    }

    public synchronized KeyStore getKeyStore() throws IOException, GeneralSecurityException
    {
        if (this.keyStore == null)
        {
            this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try
            {
                ByteArrayInputStream bais = new ByteArrayInputStream(Configuration.get(Configuration.KEY_STORE, byte[].class));
                this.keyStore.load(bais, Security.getPassword());
            }
            catch (Exception e)
            {
                LoggerFactory.getLogger(Security.class).warn("Could not load key store, creating new one", e);
                this.keyStore.load(null);
            }

            String guid = Configuration.get(Configuration.GUID, String.class);
            if (this.keyStore.getKey(guid, Security.getPassword()) == null)
            {
                this.resetKey(this.keyStore);
            }
            // TODO certificate expired? create new one!
        }
        return this.keyStore;
    }

    public synchronized SSLContext getSslContext()
    {
        if (this.sslContext == null)
        {
            SSLContext context = null;
            try
            {
                context = SSLContext.getInstance(Security.protocol);
                context.init(this.getKeyManagers(), this.getTrustManagers(), Controller.getInstance().getRandom());
            }
            catch (Exception e)
            {
                throw new Error("Failed to initialize the server-side SSLContext", e);
            }

            this.sslContext = context;
        }
        return this.sslContext;
    }

    private synchronized TrustManager[] getTrustManagers()
    {
        if (this.trustManagers == null)
        {
            TrustManager trustManager = new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                    System.out.println("trustmanager checkClientTrusted");
                    this.checkTrusted(chain, authType);
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                    System.out.println("trustmanager checkServerTrusted");
                    this.checkTrusted(chain, authType);
                }

                public void checkTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                    Certificate cert = null;
                    try
                    {
                        cert = Security.this.getTrustStore().getCertificate(chain[0].getSubjectX500Principal().getName());
                        if (cert == null)
                        {
                            throw new CertificateException("Certificate is not trusted: " + chain[0]);
                        }
                    }
                    catch (Exception e)
                    {
                        throw new CertificateException("Error while validating certificate: " + chain[0], e);
                    }
                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };
            this.trustManagers = new TrustManager[] { trustManager };
        }
        return this.trustManagers;
    }

    protected synchronized KeyStore getTrustStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
    {
        if (this.trustStore == null)
        {
            this.trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try
            {
                this.trustStore.load(new ByteArrayInputStream(Configuration.get(Configuration.TRUST_STORE, byte[].class)), Security.getPassword());
            }
            catch (Exception e)
            {
                LoggerFactory.getLogger(Security.class).warn("Could not load trust store, creating new one", e);
                this.trustStore.load(null);
            }
        }
        return this.trustStore;
    }

    public synchronized void resetKey() throws IOException, GeneralSecurityException
    {
        this.resetKey(this.getKeyStore());
    }

    private synchronized void resetKey(KeyStore ks) throws IOException, GeneralSecurityException
    {
        LoggerFactory.getLogger(Security.class).info("Creating a new key pair and certificate");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024, Controller.getInstance().getRandom());
        KeyPair kp = kpg.generateKeyPair();

        PrivateKey key = kp.getPrivate();
        String dn = "cn=" + Configuration.get(Configuration.GUID, String.class);
        X509Certificate[] chain = new X509Certificate[] { Security.generateCertificate(dn, kp, "SHA1withRSA") };

        ks.setKeyEntry(Configuration.get(Configuration.GUID, String.class), key, Security.getPassword(), chain);
        Security.pushKeyStoreToConfig(ks, Configuration.KEY_STORE);

        this.addCertificate(Configuration.get(Configuration.GUID, String.class), chain[0]);
    }
}

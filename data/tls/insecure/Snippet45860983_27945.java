public class CustomTrustManager implements X509TrustManager {

    private final static Logger logger = Logger.getLogger(CertificateManager.class);

    private static SSLSocketFactory socketFactory;
    private static CustomTrustManager instance = new CustomTrustManager();
    private static List<CertificateManager> register = new ArrayList<>();

    public static CustomTrustManager getInstance() {
        return instance;
    }

    private X509TrustManager defaultTm;

    public void register(CertificateManager certificateManager) {
        for(CertificateManager manager : register) {
            if(manager == certificateManager) {
                logger.info("Certificate manager already registered");
                return;
            }
        }
        register.add(certificateManager);
        logger.info("New Certificate manager registered");
    }

    private CustomTrustManager() {
        try {
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);

            tmf.init((KeyStore) null);
            boolean found = false;
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    defaultTm = (X509TrustManager) tm;
                    found = true;
                    break;
                }
            }
            if(found) {
                logger.info("Default trust manager found");
            } else {
                logger.warn("Default trust manager was not found");
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{this}, null);
            SSLContext.setDefault(sslContext);
            socketFactory = sslContext.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);


            logger.info("Custom trust manager was set");
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.warn("Custom trust manager can't be set");
            e.printStackTrace();
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        List<X509Certificate> out = new ArrayList<>();
        if (defaultTm != null) {
            out.addAll(Arrays.asList(defaultTm.getAcceptedIssuers()));
        }
        int defaultCount = out.size();
        logger.info("Default trust manager contain " + defaultCount + " certficates");
        for(CertificateManager manager : register) {
            X509TrustManager customTrustManager = manager.getMytrustManager();
            X509Certificate[] issuers = customTrustManager.getAcceptedIssuers();
            out.addAll(Arrays.asList(issuers));
        }
        logger.info("Custom trust managers contain " + (out.size() - defaultCount) + " certficates");
        X509Certificate[] arrayOut = new X509Certificate[out.size()];
        return out.toArray(arrayOut);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
        for(CertificateManager certificateManager : register) {
            X509TrustManager customTrustManager = certificateManager.getMytrustManager();
            try {
                customTrustManager.checkServerTrusted(chain, authType);
                logger.info("Certificate chain (server) was aproved by custom trust manager");
                return;
            } catch (Exception e) {
            }
        }
        if (defaultTm != null) {
            defaultTm.checkServerTrusted(chain, authType);
            logger.info("Certificate chain (server) was aproved by default trust manager");
        } else {
            logger.info("Certificate chain (server) was rejected");
            throw new CertificateException("Can't check server trusted certificate.");
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
        try {
            if (defaultTm != null) {
                defaultTm.checkClientTrusted(chain, authType);
                logger.info("Certificate chain (client) was aproved by default trust manager");
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            for(CertificateManager certificateManager : register) {
                X509TrustManager customTrustManager = certificateManager.getMytrustManager();
                try {
                    customTrustManager.checkClientTrusted(chain, authType);
                    logger.info("Certificate chain (client) was aproved by custom trust manager");
                    return;
                } catch (Exception e1) {
                }
            }
            logger.info("Certificate chain (client) was rejected");
            throw new CertificateException("Can't check client trusted certificate.");
        }
    }

    public SSLSocketFactory getSocketFactory() {
        return socketFactory;
    }
}

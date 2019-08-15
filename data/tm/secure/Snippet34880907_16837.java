protected static Certificate ca;

/**
     * set self signed certificate to trust
     */
    public static void trustSelfSignedSSL() {

        try {
            // set hostname verifier to check hostname validity
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return hostname.equals(Config.HTTPS_CERTIFICATE_URL) || hostname.equals(Config.HTTPS_GOOGLE_URL);
                }
            });

            // load certificate
            hbgCa = getCert();

            // add certificate to key store
            if (null != hbgCa) {
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("hbgCa", hbgCa);

                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);
//          HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
                HttpsURLConnection.setDefaultSSLSocketFactory(createSslSocketFactory());
            }
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }


    /**
     * create a custom sslsocketfactory to trust server connections
     *
     * @return
     * @throws Exception
     */
    private static SSLSocketFactory createSslSocketFactory() throws Exception {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                // Loading the CA cert
                if (null == ca) {
                    ca = getCert();
                }

                for (X509Certificate cert : chain) {

                    // check if the certificate is the selfsigned trusted one
                    if (verifiyCertificate(ca, cert)) {
                        return;
                    }
                    // check if current certificate belongs to google
                    if (cert.getIssuerX500Principal().getName().equals("CN=Google Internet Authority G2,O=Google Inc,C=US")) {
                        return;
                    }
                }

        // if none certificate trusted throw certificate exception to tell to not trust connection
                throw new CertificateException();
            }
        }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, byPassTrustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
    }


    /**
     * verifiy a certificate against the other
     */
    private static boolean verifiyCertificate(Certificate cert1, Certificate cert2) {
        try {
            cert1.verify(cert2.getPublicKey());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * load and return selfsigned local cert
     *
     * @return
     */
    private static Certificate getCert() {

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Context appContext = HBGApplication.getWrapperContext();
            AssetManager assets = appContext.getAssets();
            InputStream caInput = assets.open(Config.HTTPS_CERTIFICATE_ASSETS_FILE);
            Certificate ca;

            try {
                return cf.generateCertificate(caInput);
            } catch (CertificateException e) {
                e.printStackTrace();
            } finally {
                caInput.close();
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

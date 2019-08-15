public ConnectionConfiguration getConfigForXMPPCon(Context context) {
        ConnectionConfiguration config = new ConnectionConfiguration(URLConstants.XMPP_HOST, URLConstants.XMPP_PORT);
        config.setSASLAuthenticationEnabled(false);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        config.setCompressionEnabled(false);
        SSLContext sslContext = null;
        try {
            sslContext = createSSLContext(context);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        config.setCustomSSLContext(sslContext);
        config.setSocketFactory(sslContext.getSocketFactory());

        return config;
 }

private SSLContext createSSLContext(Context context) throws KeyStoreException,
            NoSuchAlgorithmException, KeyManagementException, IOException, CertificateException {
        KeyStore trustStore;
        InputStream in = null;
        trustStore = KeyStore.getInstance("BKS");

        if (StringConstants.DEV_SERVER_IP.equals(URLConstants.XMPP_HOST) || StringConstants.TEST_SERVER_IP.equals(URLConstants.XMPP_HOST))
            in = context.getResources().openRawResource(R.raw.ssl_keystore_dev_test);
        else if(StringConstants.STAGE_SERVER_IP.equals(URLConstants.XMPP_HOST) || StringConstants.STAGE2_SERVER_IP.equals(URLConstants.XMPP_HOST))
            in = context.getResources().openRawResource(R.raw.ssl_keystore_stage);
        else if(StringConstants.PROD_SERVER_IP.equals(URLConstants.XMPP_HOST) || StringConstants.PROD1_SERVER_IP.equals(URLConstants.XMPP_HOST))
            in = context.getResources().openRawResource(R.raw.ssl_keystore_prod);

        trustStore.load(in, "<keystore_password>".toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(),
                new SecureRandom());
        return sslContext;
}

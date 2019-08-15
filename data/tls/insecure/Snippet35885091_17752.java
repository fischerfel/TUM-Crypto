 if (mWebSocketURI.getScheme().equalsIgnoreCase(WSS_URI_SCHEME)) {

    try {
            SSLContext sslContext=SSLContext.getInstance("TLS");
            KeyManagerFactory.getInstance("X509");
            TrustManagerFactory trustManagerFactory=TrustManagerFactory.getInstance("X509");

            KeyStore trustkeyStore=KeyStore.getInstance("BKS");
            trustkeyStore.load(MainActivity.activity.getResources().getAssets().open(KEYSTOREPATH_TRUST),
                                                          KEYSTOREPASSWORD.toCharArray());
            sslContext.init(null,trustManagerFactory.getTrustManagers(), null);

            factory=sslContext.getSocketFactory();

        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
                    e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

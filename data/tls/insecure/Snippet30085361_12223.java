Security.addProvider(new BouncyCastleProvider());
    SSLContext sslContext = SSLContext.getInstance("TLS");
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init((KeyStore)null); //this is where you would add the truststore
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
    KeyStore keyStore = KeyStore.getInstance("PKCS12"); //spongyCastle library
    keyStore.load(new FileInputStream("D:\\Documents\\VISA Direct Api\\cabcentralcert.p12"), "cabcentral".toCharArray()); //inputStream to PKCS12
    keyManagerFactory.init(keyStore, "cabcentral".toCharArray());
    //TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
    TrustManager[] trustAllCertManagers = { new X509TrustManager() { // this is vulnerable to MITM attack
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }};

    sslContext.init(keyManagerFactory.getKeyManagers(), trustAllCertManagers, new SecureRandom());
    URL url = new URL(strUrl);
    HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) url.openConnection();
    httpsUrlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

    System.out.println("Response Code : " + httpsUrlConnection.getResponseCode());
    System.out.println("Cipher Suite : " + httpsUrlConnection.getCipherSuite());

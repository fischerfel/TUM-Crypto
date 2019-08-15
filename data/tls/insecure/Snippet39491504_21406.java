 OkHttpClient okHttpClient = new OkHttpClient();
    try {
        KeyStore ksTrust = KeyStore.getInstance("BKS");
        InputStream instream = this.getResources().openRawResource(R.raw.keystore);
        ksTrust.load(instream, "secret".toCharArray());

        // TrustManager decides which certificate authorities to use.
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ksTrust);
         sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
    } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
        Log.e(TAG,"in catch");
        Log.e(TAG,e.toString());

        e.printStackTrace();
    }

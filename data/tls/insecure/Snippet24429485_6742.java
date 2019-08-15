try {
    TrustManagerFactory tmf = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
    KeyStore ks = KeyStore.getInstance("BKS");
    InputStream in = ctw.getResources()
            .openRawResource(R.raw.bks);
    ks.load(in, "password".toCharArray());
    in.close();
    tmf.init(ks);
    TrustManager[] tm = tmf.getTrustManagers();

    KeyManagerFactory kmf = KeyManagerFactory
            .getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, "password".toCharArray());

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(kmf.getKeyManagers(), tm, null);

    HttpsURLConnection
            .setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname,
                        SSLSession session) {
                    return true;
                }
            });

    HttpsURLConnection
            .setDefaultSSLSocketFactory(sc.getSocketFactory());
} catch (Exception e) {
    e.printStackTrace();
}

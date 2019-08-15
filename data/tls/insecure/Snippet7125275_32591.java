private static void trustIFNetServer() {

    try {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        KeyStore ks = KeyStore.getInstance("BKS");

        InputStream in = context.getResources().openRawResource(R.raw.mykeystore);

        String keyPassword = "password"; 

        ks.load(in, keyPassword.toCharArray());

        in.close();

        tmf.init(ks);

        TrustManager[] tms = tmf.getTrustManagers();    

        SSLContext sc = SSLContext.getInstance("TLS");

    sc.init(null, tms, new java.security.SecureRandom());

    } catch (Exception e) {
    e.printStackTrace();
    }
}

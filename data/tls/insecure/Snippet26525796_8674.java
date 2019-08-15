    public static SSLContext getFactory() throws Exception {
    KeyStore trusted = KeyStore.getInstance("BKS");

    InputStream in = context.getResources().openRawResource(R.raw.myfile);

    try {
        // Initialisation de notre keystore. On entre le mot de passe (storepass)
        trusted.load(in, "mypassword".toCharArray());
    } finally {
        in.close();
    }


    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
    tmf.init(trusted);

    SSLContext ssl_context = SSLContext.getInstance("SSL");
    ssl_context.init(null, tmf.getTrustManagers(), null);

    return ssl_context;
}

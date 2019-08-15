private SSLSocketFactory getSSLSocketFactory() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException
{
    InputStream ksInStream = getResources().openRawResource(R.raw.keystore);

    KeyStore ks = KeyStore.getInstance("BKS");
    ks.load(ksInStream, SslUtils.KEYSTORE_PASSWORD_SSL.toCharArray());

//      Certificate cert = ks.getCertificate("alias");
//      ks.setCertificateEntry("ca", cert);

    ksInStream.close();

    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(ks);

    TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, wrappedTrustManagers, null);

    return sslContext.getSocketFactory();
}

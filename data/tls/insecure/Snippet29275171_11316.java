CertificateFactory cf = CertificateFactory.getInstance("X.509");
InputStream instream = context.getResources().openRawResource(R.raw.gtux_cert);
Certificate ca;
try {
    ca = cf.generateCertificate(instream);
} finally {
    caInput.close();
}

KeyStore kStore = KeyStore.getInstance(KeyStore.getDefaultType());
kStore.load(null, null);
kStore.setCertificateEntry("ca", ca);

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm(););
tmf.init(kStore);

SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), null);

okHttpClient.setSslSocketFactory(context.getSocketFactory());

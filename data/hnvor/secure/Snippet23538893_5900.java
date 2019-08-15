KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
InputStream in = new FileInputStream(new File("C:\\Program Files (x86)\\Java\\jre7\\bin\\my.keystore"));
try {
    ks.load(in, "blahblah".toCharArray());
} finally {
    in.close();
}
SSLContext sslContext = SSLContexts.custom()
        .useTLS()
        .loadTrustMaterial(ks)
        .build();
X509HostnameVerifier verifier = new AbstractVerifier() {

    @Override
    public void verify(final String host, final String[] cns, final String[] subjectAlts) throws SSLException {
        verify(host, cns, subjectAlts, true);
    }

};

CloseableHttpClient hc = HttpClients.custom()
        .setSslcontext(sslContext)
        .setHostnameVerifier(verifier)
    .build();

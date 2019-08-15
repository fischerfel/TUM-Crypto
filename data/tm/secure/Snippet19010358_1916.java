X509TrustManager customTm = new X509ExtendedTrustManager() {
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return finalTm.getAcceptedIssuers();
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        System.out
                .println("Current method: checkServerTrusted(chain, authType)");
        finalTm.checkServerTrusted(chain, authType);
    }

    //@Override
    public void checkServerTrusted(X509Certificate[] chain,
            String authType, Socket socket) throws CertificateException {
        System.out
                .println("Current method: checkServerTrusted(chain, authType, socket)");
        finalTm.checkServerTrusted(chain, authType, socket);
    }

    //@Override
    public void checkServerTrusted(X509Certificate[] chain,
            String authType, SSLEngine engine)
            throws CertificateException {
        System.out
                .println("Current method: checkServerTrusted(chain, authType, engine)");
        finalTm.checkServerTrusted(chain, authType, engine);
    }

    // Same for client-related methods.
};

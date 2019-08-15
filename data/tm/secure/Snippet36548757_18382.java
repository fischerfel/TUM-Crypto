final TrustManager[] trustManagers = tmf.getTrustManagers();
X509ExtendedTrustManager x509ExtendedTrustManager = new X509ExtendedTrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
        checkClientTrusted(x509Certificates, s);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
        checkServerTrusted(x509Certificates, s);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
        checkClientTrusted(x509Certificates, s);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
        checkServerTrusted(x509Certificates, s);
    }

    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        for (TrustManager trustManager : trustManagers)
            ((X509TrustManager)trustManager).checkClientTrusted(x509Certificates, s);
    }

    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        for (TrustManager trustManager : trustManagers)
            ((X509TrustManager)trustManager).checkServerTrusted(x509Certificates, s);

        for (X509Certificate x509Certificate : x509Certificates) {
            Collection<List<?>> alternativeNameCollection = x509Certificate.getSubjectAlternativeNames();
            if (alternativeNameCollection != null) {
                for (List alternativeNames : alternativeNameCollection) {
                    if (alternativeNames.get(1).equals(host))
                        return;
                }
            }
        }

        throw new CertificateException("Certificate hostname and requested hostname don't match");
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
};

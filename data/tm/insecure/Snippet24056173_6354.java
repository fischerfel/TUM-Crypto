FTPSClient ftpsClient= null;
SSLContext sslContext = SSLContext.getInstance(protocol);
TrustManager tm = new X509TrustManager() {
    public X509Certificate[] getAcceptedIssuers() {
        System.out.println("getAcceptedIssuers------");
        return null;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        // TODO Auto-generated method stub
        System.out.println("checkClientTrusted------");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {
        // TODO Auto-generated method stub
        System.out.println("checkServerTrusted------");
    }
};

    sslContext.init(null, new TrustManager[] { tm }, null);//new SecureRandom());
    ftpsClient = new FTPSClient(true, sslContext);

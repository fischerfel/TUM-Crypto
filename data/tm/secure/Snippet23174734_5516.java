X509TrustManager tm = new X509TrustManager() {

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {

        X509Certificate[] trustedCerts = new X509Certificate[1];
        try{
            InputStream inStream = new FileInputStream("server.cert");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
            inStream.close();
            trustedCerts[0] = cert;
        }catch(Exception e){
            e.printStackTrace();
        }

        return trustedCerts;
    }

    @Override
    public void checkClientTrusted(
            X509Certificate[] certs, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain,
            String authType) throws CertificateException {

        boolean match = false;
        try{


            InputStream inStream = new FileInputStream("server.cert");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
            inStream.close();

            for(X509Certificate c : chain){
                if(c.equals(cert)){
                    match = true;
                }
            }
        }catch(Exception e){
            throw new CertificateException();
        }

        if(!match)
            throw new CertificateException();

    }

};

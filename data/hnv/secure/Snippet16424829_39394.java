private SSLSocketFactory newSslSocketFactory(boolean trustCert, String cert) {
    try {
        KeyStore trusted = KeyStore.getInstance("BKS");
        if(trustCert) {
            String cert_begin = "-----BEGIN CERTIFICATE-----\n";
            String end_cert = "-----END CERTIFICATE-----";
            String strCert = cert_begin + cert + end_cert;

            byte[] bytes = Base64.decode(cert.getBytes(), Base64.DEFAULT);                    
            InputStream bis = new ByteArrayInputStream(bytes);
            try {
                trusted.load(bis, "password".toCharArray());
            }
            catch(Exception e) {
                int j;
                j = 10;
            }
            finally {
                bis.close();
            }
        }
        AdditionalKeyStoresSSLSocketFactory af = new AdditionalKeyStoresSSLSocketFactory(trusted, null, mTrustCert, mCert);
        af.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        return af;
    } 
    catch (Exception e)  {
        throw new AssertionError(e);
    }
}

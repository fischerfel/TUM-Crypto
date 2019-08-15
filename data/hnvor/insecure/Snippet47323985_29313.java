static SSLSocketFactory getSSLSocketFactory(SslValidationType validationType)
    throws IOException, NoSuchAlgorithmException, CertificateException,
           KeyManagementException, KeyStoreException, UnrecoverableKeyException
{
    // First, create a 'current' KeyStore that we can provide to the SSLSocketFactory
    //
    //File capath = new File("/system/etc/security/cacert1s.bks");
    //KeyStore trustStore = KeyStore.getInstance("jks");
    KeyStore trustStore = KeyStore.getInstance(getKeystoreProperty());

    if (capath.exists() == true)
    {
        FileInputStream is = new FileInputStream(capath);
        if (is != null)
        {
            trustStore.load(is, "changeit".toCharArray());
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                logger.warn("unable to load 'trust-store' with " + capath + " - " + e.getMessage());
            }
        }
    }
    else
    {
        logger.debug("EIC : bks File does not exist");

    }

    // create the appropriate factory based on the need to validate the certificates
    //
    SSLSocketFactory sf;

    if (validationType == SslValidationType.HOST)
    {
        sf = new IgnoreCertSSLSocketFactory(trustStore);
    }
    else if (validationType == SslValidationType.PEER)
    {
        sf = new SSLSocketFactory(trustStore);
        sf.setHostnameVerifier(new IgnoreHostnameVerifier());
    }
    else if (validationType == SslValidationType.BOTH)
    {
        sf = new SSLSocketFactory(trustStore);
    }
    else
    {
        sf = new IgnoreCertSSLSocketFactory(trustStore);
        sf.setHostnameVerifier(new IgnoreHostnameVerifier());
    }

    return sf;
}

static class IgnoreHostnameVerifier implements X509HostnameVerifier
{
    /**
     * @see org.apache.http.conn.ssl.X509HostnameVerifier#verify(java.lang.String, javax.net.ssl.SSLSession)
     */
    @Override
    public boolean verify(String host, SSLSession session)
    {
        // we allow all hosts
        return true;
    }

    /**
     * @see org.apache.http.conn.ssl.X509HostnameVerifier#verify(java.lang.String, javax.net.ssl.SSLSocket)
     */
    @Override
    public void verify(String host, SSLSocket ssl) throws IOException
    {
        // nothing to do, we allow all hosts
    }

    /**
     * @see org.apache.http.conn.ssl.X509HostnameVerifier#verify(java.lang.String, java.lang.String[], java.lang.String[])
     */
    @Override
    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException
    {
        // nothing to do, we allow all hosts
    }

    /**
     * @see org.apache.http.conn.ssl.X509HostnameVerifier#verify(java.lang.String, java.security.cert.X509Certificate)
     */
    @Override
    public void verify(String host, X509Certificate cert) throws SSLException
    {
        // nothing to do, we allow all hosts
    }

}

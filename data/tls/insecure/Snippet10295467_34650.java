public class MyCustomSecureSocketFactory extends JSSESocketFactory
{
    // ...
    protected SSLContext getContext() throws Exception
    {
        char[] keyStorePass = "mypass";
        java.io.File keyStoreFile = new java.io.File(System.getProperty("java.home") + "/lib/security/test_client_cert.jks";
        if(!keyStoreFile.exists())
            throw new Exception("Could not read the KeyStore file");
        InputStream keyStoreIS = new java.io.FileInputStream(keyStoreFile);
        try
        {
            KeyStore keyStore = KeyStore.getInstance("jks");//2012.04.24: KeyStore.getDefaultType());
            keyStore.load(keyStoreIS, keyStorePass);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keyStorePass);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(new KeyManager[0], new TrustManager[] {new com.myapp.MyTrustManager()}, new SecureRandom());

            return sslContext;
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            keyStoreIS.close();
        }
    }
    public javax.net.ssl.SSLSocket create(java.lang.String host, int port, StringBuffer otherHeaders, BooleanHolder useFullURL) throws Exception
    {
        javax.net.ssl.SSLSocket mySSLSocket = (SSLSocket) super.create(host, port, otherHeaders, useFullURL);
        mySSLSocket.setEnabledProtocols("TLSv1,SSLv3".split(","));
        //mySSLSocket.setEnabledCipherSuites(new String[] {"SSL_RSA_WITH_RC4_128_MD5", "SSL_RSA_WITH_RC4_128_SHA", ... many more in this list, removed for brevity
        return mySSLSocket;
    }
}

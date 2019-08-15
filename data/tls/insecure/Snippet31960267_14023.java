private getSSLContext()
{
    /* Load the keyStore that includes self-signed cert as a "trusted" entry. */
    KeyStore keyStore = ...  //optional
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()); //optional
    tmf.init(keyStore); //optional

    //This is the important line, specifying the cipher to use and cipher provider
    SSLContext sslContext = SSLContext.getInstance("TLSv1","AndroidOpenSSL");
    ctx.init(null, tmf.getTrustManagers(), null); //if trustmanager not used pass null as the second parameter    
    return sslContext;
}

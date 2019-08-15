public static TrustManager[] createTrustManagerWhoTrustAllways()
{
    return new TrustManager[]
    {
        new X509TrustManager()
        {
            @Override
            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType)
            {
                return;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType)
            {
                return;
            }
        }
    };
}

public static SSLContext loadPkcs12CertificateAndGetSslContext(String FileName, String Password) throws Exception
{
    KeyStore KS = KeyStore.getInstance("PKCS12");
    KS.load(new FileInputStream(FileName), Password.toCharArray());

    KeyManagerFactory KMF = KeyManagerFactory.getInstance("SunX509");
    KMF.init(KS, Password.toCharArray());

    SSLContext SSLC = SSLContext.getInstance("TLS");
    SSLC.init(KMF.getKeyManagers(), createTrustManagerWhoTrustAllways(), new SecureRandom());

    return SSLC;
}

public void connect() throws Exception
{
    if(true == m_Secure)
    {
        SSLContext SSLC = loadPkcs12CertificateAndGetSslContext(m_CertificateFileName, m_CertificatePassword);

        m_Socket = SSLC.getSocketFactory().createSocket(m_ServerName, m_ServerPort);
    }
    else
    {
        m_Socket = new Socket(m_ServerName, m_ServerPort);
    }
}

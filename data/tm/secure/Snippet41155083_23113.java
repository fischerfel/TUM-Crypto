public static Socket mkSocket(String host, int port, final String expected64) throws CertificateException
{
    TrustManager[] trustOnlyServerCert = new TrustManager[]
    {new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String alg)
                {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String alg) throws CertificateException
                {
                    //Get the certificate encoded as ascii text. Normally a certificate can be opened
                    //  by a text editor anyways.
                    byte[] serverCertDump = chain[0].getEncoded();
                    String server64 = Base64.encodeToString(serverCertDump, Base64.NO_PADDING & Base64.NO_WRAP);

                    //Trim the expected and presented server ceritificate ascii representations to prevent false
                    //  positive of not matching because of randomly appended new lines or tabs or both.
                    server64 = server64.trim();
                    String expected64Trimmed = expected64.trim();
                    if(!expected64Trimmed.equals(server64))
                    {
                        throw new CertificateException("Server certificate does not match expected one.");
                    }

                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }

            }
    };
    try
    {
        SSLContext context;
        context = SSLContext.getInstance("TLSv1.2");
        context.init(new KeyManager[0], trustOnlyServerCert, new SecureRandom());
        SSLSocketFactory mkssl = context.getSocketFactory();
        Socket socket = mkssl.createSocket(host, port);
        socket.setKeepAlive(true);
        return socket;
    }
    catch (Exception e)
    {
        dumpException(tag, e);
        return null;
    }
}

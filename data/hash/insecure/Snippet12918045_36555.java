static X509TrustManager tm = new X509TrustManager()
{

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException
    {
        // TODO Auto-generated method stub
    }

    @SuppressWarnings("synthetic-access")
    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException
    {


    }

    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        // TODO Auto-generated method stub
        return null;
    }

    // public static X509Certificate[] getChain() { return chain; }

};


TrustManager[] tm1 = { tm };
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, tm1, new SecureRandom());
sslFactory = sslContext.getSocketFactory();

sslSocket = (SSLSocket) sslFactory.createSocket("localhost", port);
// timeout: 10secs
sslSocket.setSoTimeout(10 * 1000); 
sslSocket.startHandshake();

// add certificate to keystore
X509Certificate[] vCenterServerCert = chain;

 for (int i = 0; i < vCenterServerCert.length; i++) {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] der = vCenterServerCert[i].getEncoded();
            md.update(der);
            byte[] digest = md.digest();

            System.out.print("-----BEGIN CERTIFICATE-----\n");
            System.out.print(hexify(digest));
            //System.out.print(new sun.misc.BASE64Encoder().encode(servercerts[i].getEncoded()));
            System.out.print("\n-----END CERTIFICATE-----\n");

        }

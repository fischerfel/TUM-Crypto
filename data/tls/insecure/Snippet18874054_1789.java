InputStream pemStream = getClass().getResourceAsStream("/Resources/cacert.pem");
HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
con.setSSLSocketFactory(getSocketFactoryFromPEM(pemStream));
con.setRequestMethod("GET");
con.setDoInput(true);
con.setDoOutput(false);
con.connect();
InputStream connectionStream = con.getInputStream();


private SSLSocketFactory getSocketFactoryFromPEM(InputStream pemStream) throws Exception
{
    byte[] certAndKey = streamToBytes(pemStream);
    byte[] certBytes = parseDERFromPEM(certAndKey, "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
    X509Certificate cert = generateCertificateFromDER(certBytes);
    KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(null);
    keystore.setCertificateEntry("cert-alias", cert);
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(keystore);
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);
    return context.getSocketFactory();
}

 private static byte[] streamToBytes(InputStream fileStream) throws IOException
{
    ByteArrayOutputStream ous = null;
    try
    {
        byte[] buffer = new byte[4096];
        ous = new ByteArrayOutputStream();
        int read = 0;
        while ((read = fileStream.read(buffer)) != -1)
            ous.write(buffer, 0, read);
    }
    finally
    {
        try
        {
            if (ous != null)
                ous.close();
        }
        catch (IOException e)
        {
            // swallow, since not that important
        }
        try
        {
            if (fileStream != null)
                fileStream.close();
        }
        catch (IOException e)
        {
            // swallow, since not that important
        }
    }
    return ous.toByteArray();
}

private static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter)
{
    String data = new String(pem);
    String[] tokens = data.split(beginDelimiter);
    tokens = tokens[1].split(endDelimiter);
    return DatatypeConverter.parseBase64Binary(tokens[0]);
}

private static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException
{
    CertificateFactory factory = CertificateFactory.getInstance("X.509");
    return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
}

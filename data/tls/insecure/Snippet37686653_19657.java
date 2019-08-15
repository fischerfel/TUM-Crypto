public static byte[] doPost(String urlString, HashMap<String, String> postData, String certificateName) throws Exception
{
    byte[] result = null;

    // Load CAs from an InputStream
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    InputStream CAInput = new BufferedInputStream(new FileInputStream(certificateName));
    Certificate certificate;

    certificate = certificateFactory.generateCertificate(CAInput);
    Dev.debug("Certificate: " + ((X509Certificate)certificate).getSubjectDN());
    CAInput.close();

    // Create Keystore containing our trusted certificates
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null, null);
    keyStore.setCertificateEntry("tss_certificate", certificate);

    // Create a TrustManager that trusts the CA in our KeyStore
    String algorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
    tmf.init(keyStore);

    // Create an SSLContext that uses our TrustManager
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);

    // Create URL and connection
    // The url string is "keystore.crt"
    URL url = new URL(urlString);
    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

    // Set connection properties
    connection.setSSLSocketFactory(context.getSocketFactory());
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    connection.setDoOutput(true);
    connection.setDoInput(true);

    // Create an output stream and write encoded data to the stream
    byte[] output = HttpPost.postEncode(postData).getBytes();
    OutputStream out = new BufferedOutputStream(connection.getOutputStream());
    out.write(output);
    out.flush();

    // Write to input stream
    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
    {
        InputStream in = connection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read()) > -1) baos.write(buffer, 0, read);
        result = baos.toByteArray();
    }

    connection.disconnect();

    return result;
}

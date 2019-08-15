public String getSoapData(String contentType) throws IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException {
    StringBuilder retVal = new StringBuilder();

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

    // get user password and file input stream
    char[] password = "MYPass".toCharArray();

    java.io.FileInputStream fis = null;
    //X509Certificate caCert = null;
    //CertificateFactory cf = CertificateFactory.getInstance("X.509");

    try {
        fis = new java.io.FileInputStream("C:\\MyCerts\\dev_HubExplorer1.pfx");
        ks.load(fis, password);
        //caCert = (X509Certificate)cf.generateCertificate(fis);
        //ks.setCertificateEntry("caCert", caCert);
    } catch (Exception e) {
        Common.screenPrint("Exception while importing certificate:%s%s", Common.CRLF, e.getMessage());
    } finally {
        if (fis != null) {
            fis.close();
        }
    }       

    tmf.init(ks);

    //TODO (GWL) Need to get this from a configuration file/db.
    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

    URL url = new URL(_publicRecordURL);
    HttpsURLConnection httpConn = (HttpsURLConnection) url.openConnection();
    httpConn.setSSLSocketFactory(sslContext.getSocketFactory());
    byte[] bytes = _requestTemplate.getBytes();

    // Set the appropriate HTTP parameters.
    httpConn.setRequestProperty("Content-Length", String.valueOf( bytes.length ) );
    httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
    httpConn.setRequestProperty("SOAPAction",_soapAction);
    httpConn.setRequestProperty("Accept","text/xml");
    httpConn.setRequestMethod( "POST" );
    httpConn.setDoOutput(true);
    httpConn.setDoInput(true);

    //Everything's set up; send the XML that was read in to b.
    OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
    writer.write(_requestTemplate);
    writer.flush();

    //Read the response and write it to standard out.
    InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
    BufferedReader in = new BufferedReader(isr);

    String inputLine;

    while ((inputLine = in.readLine()) != null) {
        retVal.append(inputLine + Common.CRLF);
        System.out.println(inputLine);
    }

    in.close();

    return retVal.toString();
}

    try {
        url = new URL(imageUrl);
        logger.info("url     "+url);

    KeyStore trustStore = KeyStore.getInstance("PKCS12");
    trustStore.load(new FileInputStream("/was85/resources/security/ecommerce_gr_mobile.p12"), "Pass".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    TrustManager[] tms = tmf.getTrustManagers();

    SSLContext sslContext = null;
    sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, tms, new SecureRandom());

    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
    logger.info("con     "+con);
    //con.setSSLSocketFactory(sslFactory);

    InputStream input = con.getInputStream(); 
    logger.info("input   " + input);

    byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(input);
    logger.info("bytes   " + bytes);
    input.close();
    imageDataString = encodeImage(bytes);
    logger.info("imageDataString   " + imageDataString);
    //return imageDataString;


} catch (MalformedInputException malformedInputException) {
    malformedInputException.printStackTrace();
    imageDataString = malformedInputException.toString();
    logger.info("MalformedInputException malformedInputException   " + imageDataString);
    return ("exception while reading the imag <" + imageDataString + ">");
} catch (IOException ioException) {
    ioException.printStackTrace();
    imageDataString = ioException.toString();
    logger.info("IOException ioException   " + imageDataString);
    return ("exception while reading the imag <" + imageDataString + ">");
} catch (KeyStoreException keyStoreException) {
    // TODO Auto-generated catch block
    keyStoreException.printStackTrace();
    imageDataString = keyStoreException.toString();
    logger.info("keyStoreException   " + imageDataString);
} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
    // TODO Auto-generated catch block
    noSuchAlgorithmException.printStackTrace();
    imageDataString = noSuchAlgorithmException.toString();
    logger.info("noSuchAlgorithmException   " + imageDataString);
} catch (CertificateException certificateExceptione) {
    // TODO Auto-generated catch block
    certificateExceptione.printStackTrace();
    imageDataString = certificateExceptione.toString();
    logger.info("certificateExceptione   " + imageDataString);
} catch (KeyManagementException keyManagementException) {
    // TODO Auto-generated catch block
    keyManagementException.printStackTrace();
    imageDataString = keyManagementException.toString();
    logger.info("keyManagementException   " + imageDataString);
}
return imageDataString;

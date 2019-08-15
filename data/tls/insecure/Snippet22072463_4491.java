private SSLSocketFactory getSSLFactory() throws Exception {
    KeyStore keyStore = KeyStore.getInstance("JKS");
    String keystoreFile = 'D:\\grails_wide\\certificates\\truststore.jks'
    File binaryFile = new File(keystoreFile);
    InputStream is = binaryFile.newInputStream()
    if (is == null) {
        return null;
    }
    String password = "****";
    keyStore.load(is, password.toCharArray());
    is.close();


    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(keyStore);

    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(null, tmf.getTrustManagers(), null);
    return ctx.getSocketFactory();
}

def accountInfo(String cardNumber, Cookie[] cookies) {
    String urlStr = "https://app-domain.com";
    URL url = new URL(urlStr);

    HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
    conn.setSSLSocketFactory(getSSLFactory());
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Cookie", WebUtils.buildCookiesHeader(cookies))
    InputStream is = conn.getInputStream();
    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    String line;
    StringBuffer stringBuffer = new StringBuffer();
    while ((line = rd.readLine()) != null) {
        stringBuffer.append(line);
    }
    rd.close();
    def response = stringBuffer.toString();

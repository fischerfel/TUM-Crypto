private Logger logger = Logger.getLogger(SSLExcludeCipherConnectionHelper.class);

private String[] exludedCipherSuites = { "_DHE_", "_DH_" };

private String trustCert = null;

private TrustManagerFactory tmf;

public void setExludedCipherSuites(String[] exludedCipherSuites) {
    this.exludedCipherSuites = exludedCipherSuites;
}

public SSLExcludeCipherConnectionHelper(String trustCert) {
    super();
    this.trustCert = trustCert;
    // Security.addProvider(new BouncyCastleProvider());
    try {
        this.initTrustManager();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

private void initTrustManager() throws Exception {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");

    InputStream caInput = new ByteArrayInputStream(trustCert.getBytes());

    Certificate ca = null;
    try {
        ca = cf.generateCertificate(caInput);
        logger.debug("ca=" + ((X509Certificate) ca).getSubjectDN());
    } finally {
        caInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    KeyStore keyStore = KeyStore.getInstance("jks");
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);
}

public String get(URL url) throws Exception {
    // Create an SSLContext that uses our TrustManager
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);
    SSLSocketFactory osf=context.getSocketFactory();


    //SSLParameters params = context.getSupportedSSLParameters();
    List<String> enabledCiphers = new ArrayList<String>();


    for (String cipher : osf.getSupportedCipherSuites()) {
        boolean exclude = false;
        if (exludedCipherSuites != null) {
            for (int i = 0; i < exludedCipherSuites.length && !exclude; i++) {
                exclude = cipher.indexOf(exludedCipherSuites[i]) >= 0;
            }
        }
        if (!exclude) {
            enabledCiphers.add(cipher);
        }
    }

    String[] cArray = new String[enabledCiphers.size()];




    enabledCiphers.toArray(cArray);

    // Tell the URLConnection to use a SocketFactory from our SSLContext
    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

    urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
    urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

    urlConnection.setRequestMethod("GET");
    urlConnection.setUseCaches(false);
    urlConnection.setAllowUserInteraction(true);
    urlConnection.setRequestProperty("Content-Type", "text/html");

    SSLSocketFactory sf = context.getSocketFactory();
    sf = new DOSSLSocketFactory(sf, cArray);
    urlConnection.setSSLSocketFactory(sf);

    BufferedReader rd = null;

    if (urlConnection.getResponseCode() == 200) {
        rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    } else {
        rd = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
    }


    StringBuffer result = new StringBuffer();
    String line = "";
    while ((line = rd.readLine()) != null) {
        result.append(line);
    }

    return result.toString();

}

public String post(URL url, String json) throws Exception {
    // Create an SSLContext that uses our TrustManager
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);
    SSLSocketFactory osf=context.getSocketFactory();


    List<String> enabledCiphers = new ArrayList<String>();

    for (String cipher : osf.getSupportedCipherSuites()) {
        boolean exclude = false;
        if (exludedCipherSuites != null) {
            for (int i = 0; i < exludedCipherSuites.length && !exclude; i++) {
                exclude = cipher.indexOf(exludedCipherSuites[i]) >= 0;
            }
        }
        if (!exclude) {
            enabledCiphers.add(cipher);
        }
    }

    String[] cArray = new String[enabledCiphers.size()];
    enabledCiphers.toArray(cArray);

    // Tell the URLConnection to use a SocketFactory from our SSLContext
    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
    SSLSocketFactory sf = context.getSocketFactory();
    sf = new DOSSLSocketFactory(sf, cArray);
    urlConnection.setSSLSocketFactory(sf);

    urlConnection.setDoOutput(true);
    urlConnection.setRequestMethod("POST");

    urlConnection.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
    urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
    urlConnection.setUseCaches(false);
    urlConnection.setAllowUserInteraction(true);
    urlConnection.setRequestProperty("Content-Type", "application/json");


     OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
     wr.write(json);
     wr.close();




    BufferedReader rd = null;

    if (urlConnection.getResponseCode() == 200) {
        rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    } else {
        rd = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
    }

    StringBuffer result = new StringBuffer();
    String line = "";
    while ((line = rd.readLine()) != null) {
        result.append(line);
    }

    return result.toString();

}

rivate class DOSSLSocketFactory extends javax.net.ssl.SSLSocketFactory {

    private SSLSocketFactory sf = null;
    private String[] enabledCiphers = null;

    private DOSSLSocketFactory(SSLSocketFactory sf, String[] enabledCiphers) {
        super();
        this.sf = sf;
        this.enabledCiphers = enabledCiphers;
    }

    private Socket getSocketWithEnabledCiphers(Socket socket) {
        if (enabledCiphers != null && socket != null && socket instanceof SSLSocket)
            ((SSLSocket) socket).setEnabledCipherSuites(enabledCiphers);

        return socket;
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return getSocketWithEnabledCiphers(sf.createSocket(s, host, port, autoClose));
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return sf.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        if (enabledCiphers == null)
            return sf.getSupportedCipherSuites();
        else
            return enabledCiphers;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return getSocketWithEnabledCiphers(sf.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port) throws IOException {
        return getSocketWithEnabledCiphers(sf.createSocket(address, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort) throws IOException,
            UnknownHostException {
        return getSocketWithEnabledCiphers(sf.createSocket(host, port, localAddress, localPort));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localaddress, int localport)
            throws IOException {
        return getSocketWithEnabledCiphers(sf.createSocket(address, port, localaddress, localport));
    }

}

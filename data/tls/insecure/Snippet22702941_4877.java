/**
 * Opens the socket with the default or a previously saved certificate
 *
 * @return A special TrustManager to save the certificate if necessary
 * @throws IOException
 */
private SavingTrustManager openSocketWithCert() throws IOException {
try {
    // Load the default KeyStore or a saved one
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    File file = new File("certs/" + server + ".cert");
    if (!file.exists() || !file.isFile())
        file = new File(System.getProperty("java.home") + "/lib/security/cacerts");

    InputStream in = new FileInputStream(file);
    ks.load(in, passphrase);

    // Set up the socket factory with the KeyStore
    SSLContext context = SSLContext.getInstance("TLS");
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);
    X509TrustManager defaultTrustManager = (X509TrustManager)tmf.getTrustManagers()[0];
    SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
    context.init(null, new TrustManager[] { tm }, null);
    SSLSocketFactory factory = context.getSocketFactory();

    sslsocket = (SSLSocket)factory.createSocket(server, port);

    return tm;
}
catch (Exception e) {
    // Hitting an exception here is very bad since we probably won't
    // recover
    // (unless it's a connectivity issue)

    // Rethrow as an IOException
    throw new IOException(e.getMessage());
}
}

/**
 * Downloads and installs a certificate if necessary
 *
 * @throws IOException
 */
private void getCertificate() throws IOException {
try {
    SavingTrustManager tm = openSocketWithCert();

    // Try to handshake the socket
    boolean success = false;
    try {
        sslsocket.startHandshake();
        success = true;
    }
    catch (SSLException e) {
        sslsocket.close();
    }

    // If we failed to handshake, save the certificate we got and try
    // again
    if (!success) {
        // Set up the directory if needed
        File dir = new File("certs");
        if (!dir.isDirectory()) {
            dir.delete();
            dir.mkdir();
        }

        // Reload (default) KeyStore
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        File file = new File(System.getProperty("java.home") + "/lib/security/cacerts");

        InputStream in = new FileInputStream(file);
        ks.load(in, passphrase);

        // Add certificate
        X509Certificate[] chain = tm.chain;
        if (chain == null)
            throw new Exception("Failed to obtain server certificate chain");

        X509Certificate cert = chain[0];
        String alias = server + "-1";
        ks.setCertificateEntry(alias, cert);

        // Save certificate
        OutputStream out = new FileOutputStream("certs/" + server + ".cert");
        ks.store(out, passphrase);
        out.close();
        System.out.println("Installed cert for " + server);
    }
}
catch (Exception e) {
    // Hitting an exception here is very bad since we probably won't
    // recover
    // (unless it's a connectivity issue)

    // Rethrow as an IOException
    e.printStackTrace();
    throw new IOException(e.getMessage());
}
}

/**
 * Attempts to connect given the previous connection information
 *
 * @throws IOException
 */
public void connect() throws IOException {
try {
    sslsocket = (SSLSocket)SSLSocketFactory.getDefault().createSocket(server, port);
    in = new BufferedInputStream(sslsocket.getInputStream());
    out = new DataOutputStream(sslsocket.getOutputStream());

    doHandshake();
}
catch (IOException e) {
    // If we failed to set up the socket, assume it's because we needed
    // a certificate
    getCertificate();
    // And use the certificate
    openSocketWithCert();

    // And try to handshake again
    in = new BufferedInputStream(sslsocket.getInputStream());
    out = new DataOutputStream(sslsocket.getOutputStream());

    doHandshake();
}

// Start reading responses
pr = new RTMPPacketReader(in);

// Handle preconnect Messages?
// -- 02 | 00 00 00 | 00 00 05 | 06 00 00 00 00 | 00 03 D0 90 02

// Connect
Map<String, Object> params = new HashMap<String, Object>();
params.put("app", app);
params.put("flashVer", "WIN 10,1,85,3");
params.put("swfUrl", swfUrl);
params.put("tcUrl", "rtmps://" + server + ":" + port);
params.put("fpad", false);
params.put("capabilities", 239);
params.put("audioCodecs", 3191);
params.put("videoCodecs", 252);
params.put("videoFunction", 1);
params.put("pageUrl", pageUrl);
params.put("objectEncoding", 3);

byte[] connect = aec.encodeConnect(params);

out.write(connect, 0, connect.length);
out.flush();

while (!results.containsKey(1))
    sleep(10);
TypedObject result = results.get(1);
DSId = result.getTO("data").getString("id");

connected = true;
}

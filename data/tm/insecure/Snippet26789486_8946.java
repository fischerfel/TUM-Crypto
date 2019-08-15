try {
    // Taken from: http://blog.alcor.se/index.php/2013/08/09/using-java-to-connect-with-sslsocket-trusting-all-certificates/
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[]
                                               chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[]
                                               chain, String authType) throws CertificateException {
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
    };

    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());

    SSLSocketFactory sslsocketfactory = sc.getSocketFactory();
    sslsocket = (SSLSocket) sslsocketfactory.createSocket(targetIp, port);
    sslsocket.setKeepAlive(true);
    // Sets this socket's read timeout in milliseconds.
    // sslsocket.setSoTimeout(timeoutSocket);

    Log.d(TAG, "Sending: " + xmlAction + xmlValue + XmlConstants.NEW_LINE);
    String line;
    ArrayList<String> receivedLines = new ArrayList<String>();

    DataOutputStream outToServerSSL = new DataOutputStream(sslsocket.getOutputStream());
    BufferedReader inFromServerSSL = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
    outToServerSSL.writeBytes(xmlAction + xmlValue + XmlConstants.NEW_LINE + XmlConstants.NEW_LINE);

    while ((line = inFromServerSSL.readLine()) != null) {
        receivedLines.add(line);
        Log.d(TAG, "Received: " + line);
    }

    Log.d(TAG, "Closing tha sizzle");
    inFromServerSSL.close();
    outToServerSSL.close();

    return ParseXmlForStatusCode(receivedLines);

} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (UnknownHostException e) {
    e.printStackTrace();
} catch (KeyManagementException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (sslsocket != null) {
        try {
            sslsocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

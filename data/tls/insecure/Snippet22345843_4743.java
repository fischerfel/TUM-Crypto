public final class ServerCommunicator {

private static final int TIMEOUT = 30000;

public static long counter = 0;

public static String sendToTestTool(String url, String dataToSend) {
    String encodedString = null;
    counter = counter + 1;
    System.out.println("Counter :" + counter);
    try {
        encodedString = getBase64EncodedString(dataToSend);
        return sendToServer(encodedString, url);
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return null;
}

public static String sendToWSCServer(String dataToSend, String url) {
    counter = counter + 1;
    System.out.println("Counter :" + counter);
    return sendToServer(dataToSend, url);
}

private static String sendToServer(String dataToSend, String url) {

    HttpURLConnection connections = null;
    try {

        URL urls = new URL(url);

        if (urls.getProtocol().equals("https")) {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs,
                        String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs,
                        String authType) {
                }
            } };

            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts,
                        new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc
                        .getSocketFactory());
            } catch (Exception e) {
            }
            HttpsURLConnection
                    .setDefaultHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String hostname,
                                SSLSession session) {
                            return true;
                        }
                    });
            connections = (HttpsURLConnection) urls.openConnection();
        } else {
            connections = (HttpURLConnection) urls.openConnection();
        }

        connections.setRequestMethod("POST");
        connections.setDoInput(true);
        connections.setDoOutput(true);
        connections.setConnectTimeout(TIMEOUT);

        connections.getOutputStream().write(dataToSend.getBytes());

        InputStream inputStream = connections.getInputStream();
        String responString = null;
        switch (connections.getResponseCode()) {

        case HttpURLConnection.HTTP_OK:
            long contentLength = connections.getContentLength();
            byte[] read = read(inputStream, contentLength);
            responString = new String(read);
            break;
        default:
            break;
        }

        return discardWhitespace(responString);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (connections != null) {
            connections.disconnect();
            connections = null;
        }
    }
    return null;

}

public static byte[] read(InputStream in, long length) throws IOException {

    // long length = in.available();

    if (length > Integer.MAX_VALUE) {
        throw new IOException("File is too large");
    }

    byte[] bytes = new byte[(int) length];

    int offset = 0;
    int numRead = 0;

    while (offset < bytes.length
            && (numRead = in.read(bytes, offset, bytes.length - offset)) != -1) {
        offset += numRead;

    }

    if (offset < bytes.length) {
        throw new IOException("Could not completely read file ");
    }

    in.close();
    return bytes;
}

private static String getBase64EncodedString(String sendToServer)
        throws UnsupportedEncodingException {
    byte[] data = sendToServer.getBytes("UTF-8");
    String encodedString = Base64.encodeToString(data, Base64.DEFAULT);
    return encodedString;
}

private static String discardWhitespace(String data) {
    if (data == null) {
        return null;
    }
    StringBuffer groomedData = new StringBuffer("");
    for (int i = 0; i < data.length(); i++) {
        switch (data.charAt(i)) {
        case (byte) '\n':
        case (byte) '\r':
            break;
        default:
            groomedData.append(data.charAt(i));
        }
    }
    return groomedData.toString();

}

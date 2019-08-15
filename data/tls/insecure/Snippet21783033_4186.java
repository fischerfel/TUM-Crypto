public void btnclick(View v) {

for (int i = 0; i < 10; i++) {
    String pathToOurFile = "/mnt/sdcard/" + "1.png";
    String urlServer = "https://google/post.aspx";
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1 * 1024 * 1024;

    try {

        FileInputStream fileInputStream = new FileInputStream(new File(
                pathToOurFile));

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs,
                    String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs,
                    String authType) {
            }
        } };

        // Ignore differences between given hostname and certificate
        // hostname
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc
                    .getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "exception",
                    Toast.LENGTH_SHORT).show();
        }

        URL url = new URL(urlServer);
        connection = (HttpsURLConnection) url.openConnection();

        // Allow Inputs & Outputs
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("ENCTYPE", "multipart/form-data");
        connection.setRequestProperty("Content-Type",
                "multipart/form-data;boundary=" + boundary);
        connection.setRequestProperty("uploaded_file", "second");
        outputStream = new DataOutputStream(
                connection.getOutputStream());
        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        outputStream
                .writeBytes("Content-Disposition: form-data; name=\"sample\";filename=\""
                        + pathToOurFile + "\"" + lineEnd);
        outputStream.writeBytes(lineEnd);

        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        // Read file
        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                + lineEnd);

        // Responses from the server (code and message)
        int serverResponseCode = connection.getResponseCode();
        String serverResponseMessage = connection.getResponseMessage();

        Toast.makeText(getApplicationContext(),
                serverResponseCode + "," + serverResponseMessage,
                Toast.LENGTH_LONG).show();

        fileInputStream.close();
        outputStream.flush();
        outputStream.close();
    } catch (Exception ex) {
        // Exception handling
    }
}

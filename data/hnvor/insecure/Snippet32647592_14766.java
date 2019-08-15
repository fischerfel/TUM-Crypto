private static final int CONNECTION_TIMEOUT = 20000;
private static final int IMAGE_UPLOAD_TIMEOUT = 60000;

private void uploadFileHttps(File file, String requestURL) {
    HttpsURLConnection connection = null;
    try {

        //requestURL is a https link like: https://test.abcde.com/upload
        connection = (HttpsURLConnection) new URL(requestURL).openConnection();

        connection.setHostnameVerifier(new NullHostNameVerifier());

        connection.setConnectTimeout(CONNECTION_TIMEOUT);//timeout for handshake
        connection.setReadTimeout(IMAGE_UPLOAD_TIMEOUT);//timeout for waiting read data
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "image/jpeg");
        Token.setAuthTokenHeader(connection, context);

        connection.setDoOutput(true);//set to use httpURLConnection for output
        connection.connect();

        //upload file to server
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        int bytesRead;
        byte buf[] = new byte[1024];
        BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(file));
        while ((bytesRead = bufInput.read(buf)) != -1) {
            // write output
            out.write(buf, 0, bytesRead);
            out.flush();
        }
        out.flush();
        out.close();

        //get response from server
        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        System.out.println(sb.toString());
    } catch (Exception e) {
        if (e != null)
            e.printStackTrace();
    } finally {
        if (connection != null) connection.disconnect();
    }
}

class NullHostNameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        Log.i("RestUtilImpl", "Approving certificate for " + hostname);
        return true;
    }

}

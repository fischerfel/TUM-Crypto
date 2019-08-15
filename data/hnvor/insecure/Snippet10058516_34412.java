HttpURLConnection connection = (HttpURLConnection) url.openConnection();
connection.setConnectTimeout(CONNECT_TIMEOUT);

connection.setDoOutput(true); // Triggers POST.
connection.setRequestMethod("POST");
int contentLength = 0;
if(body != null) {
    contentLength = body.getBytes().length;
}

// Workarounds for older Android versions who do not do that automatically (2.3.5 for example)
connection.setRequestProperty(HTTP.TARGET_HOST, url.getHost());
connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

// Set SSL Context -- Development only
if(context != null && connection instanceof HttpsURLConnection){
    HttpsURLConnection conn = (HttpsURLConnection)connection;
    conn.setSSLSocketFactory(context.getSocketFactory());
    conn.setHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    });
}

try{
    // Add headers
    if(headers != null){
        for (NameValuePair nvp : headers) {
            if(nvp != null){
                connection.setRequestProperty(nvp.getName(), nvp.getValue());
            }
        }
    }

    connection.setFixedLengthStreamingMode(contentLength);
    OutputStream outputStream = null;
    try {
        if(body != null){
            outputStream = connection.getOutputStream();
            BufferedOutputStream stream = new BufferedOutputStream(outputStream);
            stream.write(body.getBytes()); // <<<< No effect ?!
            stream.flush();

        }
    } finally {
        if (outputStream != null) 
            try { 
                outputStream.close(); 
            }
        catch (IOException logOrIgnore) {
            // ...
        }
    }

    InputStream inputStream = connection.getInputStream();

    // .... Normal case ....

}
catch(IOException e){
    // ... Exception! Check Error stream and the response code ...


}
finally{
    connection.disconnect();
}

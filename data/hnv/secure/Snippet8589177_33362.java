URL url = new URL(url_to_send); 
trustAllHosts();
connection = (HttpsURLConnection) url.openConnection();
connection.setHostnameVerifier(DO_NOT_VERIFY);
connection.setChunkedStreamingMode(maxBufferSize);

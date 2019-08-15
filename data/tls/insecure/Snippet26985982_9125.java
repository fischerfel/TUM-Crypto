if (downloadurl.startsWith("https://")) {
   HttpsConn = (HttpsURLConnection) url.openConnection();
   HttpsURLConnection.setDefaultHostnameVerifier(new AllowAllHostNameVerifier());       

    SSLContext sc;
                sc = SSLContext.getInstance("TLS");
                sc.init(null, new TrustManager[] {
                          new X509TrustManager() {
                                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                              }
                            }, null);
                HttpsConn.setSSLSocketFactory(sc.getSocketFactory());
   HttpsConn.setSSLSocketFactory(sc.getSocketFactory());

   HttpsConn.setConnectTimeout(CONNECT_TIME_SECONDS * 1000);
   HttpsConn.setReadTimeout(READ_TIME_SECONDS * 1000);
   HttpsConn.setChunkedStreamingMode(0);  
   HttpsConn.connect();
} else {
   URLConn = (HttpURLConnection) url.openConnection();
   URLConn.setConnectTimeout(CONNECT_TIME_SECONDS * 1000);
   URLConn.setReadTimeout(READ_TIME_SECONDS * 1000);
   URLConn.setChunkedStreamingMode(0);  
   URLConn.connect();
}               

                                   .
                                   .
                                   .

byte data[] = new byte[1048576];
double currentDownloadSize = 0.0;
long startTime = System.currentTimeMillis();
lastUpdateTime = startTime;
int count;

while ((count = input.read(data)) != -1) {
    currentDownloadSize += count;
    output.write(data, 0, count);
        Thread.sleep(10);

    if (isCancelled()) {
        output.flush();
        output.close();
        input.close();
    }
}

output.flush();
output.close();
input.close();

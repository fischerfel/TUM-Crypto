<%! // Define Static Constants
    // ***********************
public static X509TrustManager s_x509TrustManager = null;
public static SSLSocketFactory s_sslSocketFactory = null;

static {
        s_x509TrustManager = new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[] {}; } 
        public boolean isClientTrusted(X509Certificate[] chain) { return true; } 
        public boolean isServerTrusted(X509Certificate[] chain) { return true; } 
    };

    java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    try {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[] { s_x509TrustManager }, null);
        s_sslSocketFactory = context.getSocketFactory();
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e.getMessage());
    }
}

...
...
           // write output to VPC
            SSLSocket ssl = (SSLSocket)s_sslSocketFactory.createSocket(s, vpc_Host, vpc_Port, true);
            ssl.startHandshake();
            os = ssl.getOutputStream();
            // get response data from VPC
            is = ssl.getInputStream();
...
...
%>

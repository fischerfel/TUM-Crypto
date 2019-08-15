System.setProperty('javax.net.ssl.keyStore', 'jksfile')
System.setProperty('javax.net.ssl.keyStorePassword', '')
System.setProperty("https.protocols", "TLSv1");

System.setProperty('javax.net.ssl.trustStore', 'C:/Program Files/Java/jdk1.7.0_79/jre/lib/security/cacerts')
System.setProperty('javax.net.ssl.trustStorePassword', '')

SSLContext sslcontext = SSLContext.getInstance("TLSv1");
        sslcontext.init(null, null, null);

    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();


    if (conn instanceof HttpsURLConnection){
        conn.setSSLSocketFactory(sslcontext.getSocketFactory());
    } 

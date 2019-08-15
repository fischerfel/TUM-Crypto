@Override
public void messageReceived(IoSession session, Object msg) {

    jsonParser(msg) //communication is in json
    if (condition) {
        startTLS();
    }

}


SslFilter sslFilter;
public void startTLS(JSONObject msg) throws GeneralSecurityException{

    TrustManager[] trustAllCerts = new TrustManager[] { 
          new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                    }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

        }};     

    try {               

        SSLContext sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(null, trustAllCerts, null);

        IoFilterChain chain = session.getFilterChain();
        sslFilter = new SslFilter(sslContext);
        sslFilter.setUseClientMode(true);               
        chain.addFirst("sslFilter", sslFilter);

    } catch(Exception e){
        e.printStackTrace();
    }
}

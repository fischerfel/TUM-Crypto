HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
connection.setRequestMethod("POST");
connection.setUseCaches(false);
connection.setDoInput(true);
connection.setDoOutput(true);
if (connection instanceof HttpsURLConnection) {
    try {
        KeyManager[] km = null;
        TrustManager[] tm = {new RelaxedX509TrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, tm, new java.security.SecureRandom());
        SSLSocketFactory sf = sslContext.getSocketFactory();
        ((HttpsURLConnection)connection).setSSLSocketFactory(sf);
        System.out.println("setSSLSocketFactory OK!");
    }catch (java.security.GeneralSecurityException e) {
        System.out.println("GeneralSecurityException: "+e.getMessage());
    }
}

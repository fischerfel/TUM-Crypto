try {
    SSLContext sc = SSLContext.getInstance("SSL"); 
    sc.init(null, null, null); 
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
} catch (GeneralSecurityException e) {
}

 SSLContext sc = SSLContext.getInstance("SSL"); sc.init(null, trustAllCerts, new SecureRandom());
 HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFa‌​ctory());
 HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

 @Override public boolean verify(String arg0, SSLSession arg1) {

       return true; 
   } 
}); 

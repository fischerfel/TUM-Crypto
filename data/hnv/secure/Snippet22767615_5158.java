httpsURLConnection = (HttpsURLConnection)url.openConnection();

if(socketFactory!=null){
    httpsURLConnection.setSSLSocketFactory(socketFactory);
} else {
   log.w("tryConnecting : socket factory is null");
}
httpsURLConnection.setHostnameVerifier(new MzHostNameVerifier());

httpsURLConnection.connect();
int responseCode = httpsURLConnection.getResponseCode();
if(responseCode == HttpURLConnection.HTTP_OK){
Certificate[] certificate = httpsURLConnection.getServerCertificates();
httpsURLConnection.disconnect();
return certificate;
} else {
log.e("Connection error, code : "+responseCode);
return null;
}

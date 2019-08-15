spnego = new SpnegoHttpURLConnection("spnego-client", <<sharepoint_user>>, <<sharepoint_password>>); 

//New Lines added to omit SSL Handshake exception 
TrustManager[] trustAllCerts = new TrustManager[]{ 
new X509TrustManager() { 
public java.security.cert.X509Certificate[] getAcceptedIssuers(){ 
return null; 
} 
public void checkClientTrusted(java.security.cert.X509Certific ate[] certs, String authType){ 
//No need to implement. 
} 
public void checkServerTrusted(java.security.cert.X509Certific ate[] certs, String authType){ 
//No need to implement. 
} 
} 
}; 
SSLContext sc = SSLContext.getInstance("SSL"); 
sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
HttpsURLConnection.setDefaultSSLSocketFactory(sc.g etSocketFactory()); 
spnego.connect(new URL(spLocation)); 
System.out.println("spnego.getResponseCode():: "+spnego.getResponseCode()); 
if(spnego.getResponseCode() >= 200) { 
log.debug("Authentication Successful"); 
} 

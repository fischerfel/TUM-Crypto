public class A{
    HostnameVerifier hv = new HostnameVerifier(){
        public boolean verify(String urlHostName, SSLSession session){
            return true;
        }       
    };

    HttpsURLConnection.setDefaultHostnameVerifier(hv);

    javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
    javax.net.ssl.TrustManager tm = new miTM();
    trustAllCerts[0] = tm;
    javax.net.ssl.SSLContext sc = null;
    try {
        sc = javax.net.ssl.SSLContext.getInstance("SSL");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    try {
        sc.init(null, trustAllCerts, null);
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }
   javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
}
class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager{

    public java.security.cert.X509Certificate[] getAcceptedIssuers(){
        return null;
    }

    public boolean isServerTrusted(java.security.cert.X509Certificate[] certs){
        return true;
        }

    public boolean isClientTrusted(java.security.cert.X509Certificate[] certs){
        return true;
    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException{
        return;
    }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException{
        return;
        }
}

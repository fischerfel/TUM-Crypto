public final class TrustAllCertificates implements X509TrustManager, HostnameVerifier
{
     public X509Certificate[] getAcceptedIssuers() {return null;}
     public void checkClientTrusted(X509Certificate[] certs, String authType) {}
     public void checkServerTrusted(X509Certificate[] certs, String authType) {}
     public boolean verify(String hostname, SSLSession session) {return true;}
     public static void install()
     {
         try
         {
            TrustAllCertificates trustAll = new TrustAllCertificates();
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{trustAll}, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(trustAll);
         }
         catch (Exeption e)
         {
            JatLog.writeTempLog("Error: " + e.getMessage());
         }
     }
}

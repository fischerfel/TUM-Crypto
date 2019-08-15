URL obj = new URL(url);
HttpsURLConnection con =  (HttpsURLConnection) obj.openConnection();
con.setHostnameVerifier(new AlwaysTrustHostnameVerifier());

class AlwaysTrustHostnameVerifier implements X509TrustManager, HostnameVerifier 
{
   @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
        // TODO Auto-generated method stub

    }
    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
        // TODO Auto-generated method stub

    }
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public boolean verify(String arg0, SSLSession arg1) {
        // TODO Auto-generated method stub
        return false;
    }      
} 

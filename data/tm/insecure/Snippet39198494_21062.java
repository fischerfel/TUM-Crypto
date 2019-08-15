class MyTrustManager implements X509TrustManager 
{
public java.security.cert.X509Certificate[] getAcceptedIssuers() {
   return null;
}

public void checkClientTrusted(X509Certificate[] certs, String authType) {
}

public void checkServerTrusted(X509Certificate[] certs, String authType) {
}

@Override
 public void checkClientTrusted(java.security.cert.X509Certificate[]      paramArrayOfX509Certificate, String paramString)
    throws CertificateException {
  // TODO Auto-generated method stub

}

@Override
public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString)
        throws CertificateException {
    // TODO Auto-generated method stub

}

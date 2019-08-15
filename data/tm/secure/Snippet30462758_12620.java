public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { 
    for (X509TrustManager tm : x509TrustManagers) { 
        try { 
            tm.checkServerTrusted(chain, authType); 
            return; 
        } catch (CertificateException e) { /* ignore */ } 
    } 
    throw new CertificateException(); 
}

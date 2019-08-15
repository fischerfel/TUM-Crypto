 public void checkServerTrusted(java.security.cert.X509Certificate[] 
                   chain, String authType) throws CertificateException {
        ((X509TrustManager) trustManager.checkServerTrusted(chain, authType);
    }

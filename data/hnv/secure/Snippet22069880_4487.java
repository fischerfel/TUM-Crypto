if ("https".equals(url.getProtocol()) && mSslSocketFactory != null) {            
    ((HttpsURLConnection)connection).setSSLSocketFactory(mSslSocketFactory);
    ((HttpsURLConnection)connection).setHostnameVerifier(new CustomHostnameVerifier());         
}

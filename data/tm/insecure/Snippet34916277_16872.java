Class CustomTrustManager implements X509TrustManager {

public boolean checkClientTrusted(java.security.cert.X509Certificate[] chain) {
    return true;
}

public boolean isServerTrusted(java.security.cert.X509Certificate[] chain) {
    return true;
}

public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    return null;
}

public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
}

public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
}

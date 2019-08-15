class Java2000TrustManager implements X509TrustManager {

Java2000TrustManager() {
}

public void checkClientTrusted(X509Certificate chain[], String authType)
        throws CertificateException {
    System.out.println("checkClientTrusted...");
}

public void checkServerTrusted(X509Certificate chain[], String authType)
        throws CertificateException {
    System.out.println("checkServerTrusted");
}

public X509Certificate[] getAcceptedIssuers() {
    System.out.println("getAcceptedIssuers...");
    return null;
}
}

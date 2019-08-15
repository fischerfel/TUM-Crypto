SSLContext sslContext = SSLContext.getInstance("TLS");
        X509TrustManager[] xtmArray = new X509TrustManager[] { new OwnTrustManager() };
        sslContext.init(null, xtmArray, new java.security.SecureRandom());
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
                    .getSocketFactory());
        }
        HttpsURLConnection
                .setDefaultHostnameVerifier(new ISAHostnameVerifier());



public class OwnTrustManager implements X509TrustManager{
public void checkClientTrusted(X509Certificate[] chain, String authType) {
}

public void checkServerTrusted(X509Certificate[] chain, String authType) {
}

public X509Certificate[] getAcceptedIssuers() {
    return null;
}
}


public class ISAHostnameVerifier implements HostnameVerifier{
public boolean verify(String hostname, SSLSession session) {
    return true;
}
}

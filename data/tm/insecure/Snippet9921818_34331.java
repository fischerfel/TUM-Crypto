TrustManager[] myTrustManagerArray = new TrustManager[]{new TrustEveryoneManager()};

class TrustEveryoneManager implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] arg0, String arg1){}
    public void checkServerTrusted(X509Certificate[] arg0, String arg1){}
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

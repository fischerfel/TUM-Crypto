TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
    };

SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, trustAllCerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
{
    public boolean verify(String arg0, SSLSession arg1) {
        return true;
    }
});

URL url = new URL("https://android.clients.google.com/market/api/ApiRequest");
HttpsURLConnection cnx = (HttpsURLConnection)url.openConnection();
cnx.setDoOutput(true);
cnx.setRequestMethod("POST");
cnx.setRequestProperty("Cookie","ANDROIDSECURE=" + this.getAuthSubToken());
cnx.setRequestProperty("User-Agent", "Android-Market/2 (sapphire PLAT-RC33); gzip");
cnx.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
cnx.setRequestProperty("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
String request64 = Base64.encodeBytes(request,Base64.URL_SAFE);
String requestData = "version="+PROTOCOL_VERSION+"&request="+request64;
cnx.setFixedLengthStreamingMode(requestData.getBytes("UTF-8").length);
OutputStream os = cnx.getOutputStream();
os.write(requestData.getBytes());
os.close();

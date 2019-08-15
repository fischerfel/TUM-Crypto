public static void getWithoutCertificate() {
    String url = "https://thapi.bluepay.asia/blue/gateWay/doProcess/service_queryTrans?operatorId=2&productid=177&t_id=gasq20160519153044zD&encrypt=30001721bd69594aa09b30ccaebb7683";
    StringBuffer bufferRes = null;
    try {
        TrustManager[] tm = { new MyX509TrustManager() }; 
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // get SSLSocketFactory from the sslContext above
        javax.net.ssl.SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL _url = new URL(url);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) _url
                .openConnection();
        // trust any hostname
        httpsURLConnection
                .setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
        httpsURLConnection.setSSLSocketFactory(ssf);
        httpsURLConnection.setConnectTimeout(25000);
        httpsURLConnection.setReadTimeout(25000);
        httpsURLConnection.setRequestMethod("GET");
        httpsURLConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        httpsURLConnection
                .setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.setDoInput(true);
        httpsURLConnection.connect();
        InputStream in = httpsURLConnection.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in,
                "utf-8"));
        String valueString = null;
        bufferRes = new StringBuffer();
        while ((valueString = read.readLine()) != null) {
            bufferRes.append(valueString);
        }
        in.close();
        if (httpsURLConnection != null) {
            httpsURLConnection.disconnect();// close connection
        }
        Log.i(TAG, bufferRes.toString());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

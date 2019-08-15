       public String retrieveMngTracking(ArrayList paramList) throws Exception {

             //ı added for SSL
    HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            System.out.println("Warning: URL Host: " + urlHostName
                    + " vs. " + session.getPeerHost());
            return true;
        }
    };



    ParamMap paramMap = FlexUtil.getParamMap(paramList);

    URL url = new URL(paramMap.getString("url"));

    //Call this function for SSL
    trustAllHttpsCertificates();
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setDoInput(true);
    conn.setDoOutput(true);
    conn.setUseCaches(false);
    conn.setReadTimeout(20000);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-type",
            "application/x-www-form-urlencoded");

    OutputStream os = conn.getOutputStream();
    OutputStreamWriter wr = new OutputStreamWriter(os);

    wr.write("pMusteriNo=" + paramMap.getString("pMusteriNo"));
    wr.write("&pSifre=" + paramMap.getString("pSifre"));
    wr.write("&pSiparisNo=" + paramMap.getString("pSiparisNo"));
    wr.write("&pKriter=" + paramMap.getString("pKriter"));

    wr.flush();

    wr.close();
    os.close();

    StringBuffer sb = new StringBuffer();
    BufferedReader br = new BufferedReader(new InputStreamReader(
            conn.getInputStream()));

    String response = "";
    for (;;) {
        String line = br.readLine();
        if (line == null)
            break;
        response += line + "\n";
    }
    br.close();

    conn.disconnect();

    return response;
}
     public static class miTM implements javax.net.ssl.TrustManager,
        javax.net.ssl.X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public boolean isServerTrusted(
            java.security.cert.X509Certificate[] certs) {
        return true;
    }

    public boolean isClientTrusted(
            java.security.cert.X509Certificate[] certs) {
        return true;
    }

    public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }

    public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }
}

private static void trustAllHttpsCertificates() throws Exception {

    // Create a trust manager that does not validate certificate chains:

    javax.net.ssl.TrustManager[] trustAllCerts =

    new javax.net.ssl.TrustManager[1];

    javax.net.ssl.TrustManager tm = new miTM();

    trustAllCerts[0] = tm;

    javax.net.ssl.SSLContext sc =

    javax.net.ssl.SSLContext.getInstance("SSL");

    sc.init(null, trustAllCerts, null);

    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(

    sc.getSocketFactory());

}

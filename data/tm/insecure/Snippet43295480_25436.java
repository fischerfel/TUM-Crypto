 static public void test(){
    try {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], 
            new TrustManager[] {new X509TrustManager(){
                @Override public void checkClientTrusted(
                     X509Certificate[] arg0, String arg1) 
                throws CertificateException {}

                @Override public void checkServerTrusted(
                     X509Certificate[] arg0, String arg1) 
                throws CertificateException {}

                @Override public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }}}, new SecureRandom());
        SSLContext.setDefault(ctx);

        URL url = null;

        url = new URL("https://tlauncher.org/repo/test" +
                      "/ConfigPreMasterSALF-2.18.json");

        HttpsURLConnection conn = (HttpsURLConnectio) url.openConnection();
        conn.setHostnameVerifier(new HostnameVerifier() {
           @Override public boolean verify(String arg0, SSLSession arg1) {
              return true;
           }
        });
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            conn.getInputStream(),"utf8"));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        System.out.println(response.toString());
        conn.disconnect();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

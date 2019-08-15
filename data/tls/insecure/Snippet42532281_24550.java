public static void main(String[] args) {
    try {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new FileInputStream("d:\\certs\\api\\xx.p12"), "W*53as_G".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, "W*53as_G".toCharArray());
        KeyManager[] kms = kmf.getKeyManagers();

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("c:\\jdk1.8.0_51\\jre\\lib\\security\\cacerts"), "changeit".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        TrustManager[] tms = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(kms, tms, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        URL url = new URL("https://apis2s.ee/test");

        HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
        urlConn.setRequestProperty("Authorization", "Basic " + Base64.encode("andrey:pass_1".getBytes()));
        urlConn.setUseCaches(false);
        urlConn.setAllowUserInteraction(true);
        urlConn.setRequestProperty("Pragma", "no-cache");
        urlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        urlConn.setRequestProperty("Content-length", Integer.toString("id=1288210&ip=127.0.0.1".length()));

        StringBuilder builder = new StringBuilder();
        builder.append(urlConn.getResponseCode())
                .append(" ")
                .append(urlConn.getResponseMessage())
                .append("\n");

        System.out.println(builder);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@Override
    public String demoAPI(String xmlData) {
        StringBuilder sb = new StringBuilder();
        String output="";
    try {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new FileInputStream(new File("path-to-pfx-file")),
                "password".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, "password".toCharArray());
        KeyManager[] kms = kmf.getKeyManagers();
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("path-to-jks-file"), "password".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        TrustManager[] tms = tmf.getTrustManagers();
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kms, tms, new SecureRandom());
        SSLContext.setDefault(sslContext);
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        URL url = new URL("URL");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(100000);
        con.setSSLSocketFactory(sslContext.getSocketFactory());
        con.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        System.out.println(sb.toString());
        output = sb.toString();
    } catch (Exception e) {
        e.printStackTrace();
        output = e.getMessage();
    }
    return output;
}

public void connectCert(String jsonParams) {
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
        urlConn.setRequestProperty("Content-length", Integer.toString(jsonParams.length()));

        urlConn.setDoOutput(true);
        urlConn.setRequestProperty("Content-Length", Integer.toString(jsonParams.length()));
        PrintStream out = new PrintStream(urlConn.getOutputStream());
        out.print(jsonParams);
        out.flush();
        out.close();

        StringBuilder builder = new StringBuilder();
        int responseCode = urlConn.getResponseCode();
        builder.append(responseCode).append(" ").append(urlConn.getResponseMessage()).append("\n");

        InputStream inputStream = null;

        if (responseCode == 200) inputStream = urlConn.getInputStream();
        else inputStream = urlConn.getErrorStream();//this returns 400
        Scanner in = new Scanner(inputStream);
        String responseStr = "";
        while (in.hasNextLine()) {
            String str = in.nextLine();
            responseStr += str;
        }
        System.out.println(builder);
        System.out.println("responseStr: " + responseStr);
    } catch (Exception e) {
    }
}

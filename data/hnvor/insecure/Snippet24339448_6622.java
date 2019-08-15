public static String DoMyPost(String URL, String requestData, String transactID)
        throws MalformedURLException, Exception {
    System.out.println("DoMyPost(): Start");
    String strResponse = "";
    URL url = new URL(URL);
    try {
        HostnameVerifier hv = new HostnameVerifier() {
         @Override
         public boolean verify(String urlHostName, SSLSession session) {
         return true;
         }
         };
        System.setProperty("javax.net.ssl.keyStore", "C:\\Program Files (x86)\\Java\\jre7\\bin\\testserver1.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "wasp123");
        System.setProperty("javax.net.ssl.keyStoreType", "JKS");

        System.setProperty("javax.net.ssl.trustStore", "C:\\Program Files (x86)\\Java\\jre7\\bin\\testserver1TrustStore");
        System.setProperty("javax.net.ssl.trustStorePassword", "wasp123");
        System.setProperty("javax.net.debug", "all");

        //CertificateFactory factory2 = CertificateFactory.getInstance("X.509");
        //Certificate generateCertificate = factory2.generateCertificate(new FileInputStream("C:\\Program Files (x86)\\Java\\jre7\\bin\\testserver1.cer"));
        KeyStore ks = KeyStore.getInstance("JKS");
        InputStream certK = new FileInputStream("C:\\Program Files (x86)\\Java\\jre7\\bin\\testserver1.jks");
        ks.load(certK, "wasp123".toCharArray());
        //ks.setCertificateEntry("testserver1cert", generateCertificate);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "wasp123".toCharArray());

        KeyStore truststore = KeyStore.getInstance("JKS");
        InputStream truststorex = new FileInputStream("C:\\Program Files (x86)\\Java\\jre7\\bin\\testserver1TrustStore");
        truststore.load(truststorex, "wasp123".toCharArray());
        TrustManager[] tm;
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(truststore);
        tm = tmf.getTrustManagers();
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(kmf.getKeyManagers(), tm, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        // Open a secure connection.
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setSSLSocketFactory(sockFact);
        con.setHostnameVerifier(hv);
        // Set up the connection properties
        con.setRequestProperty("Connection", "close");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", Integer.toString(requestData.length()));

        // Send the request
        OutputStream outputStream = con.getOutputStream();
        outputStream.write(requestData.getBytes("UTF-8"));
        outputStream.close();

        // Check for errors
        int responseCode = con.getResponseCode();
        InputStream inputStream;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = con.getInputStream();
        } else {
            inputStream = con.getErrorStream();
        }
        System.out.println(inputStream);
        inputStream.close();            
        InputStreamReader isr = new InputStreamReader(con.getInputStream());
        BufferedReader in = new BufferedReader(isr);
        StringBuilder out = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            out.append(inputLine.replace("<![CDATA[", "").replace("]]>", "").replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
        }
        in.close();
        String result = out.toString();
        System.out.println(result);
        strResponse = result;
    } catch (Exception ex) {
        strResponse = "Connection Error " + ex;
        System.out.println(ex);
    }
    return strResponse;
}

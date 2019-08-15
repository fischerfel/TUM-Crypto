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

        urlConn.setDoOutput( true );
        urlConn.setRequestProperty( "Content-Length", Integer.toString( jsonParams.length() ));
        PrintStream out = new PrintStream(urlConn.getOutputStream());
        out.print("id=1288210&ip=127.0.0.1");
        out.flush();
        out.close();

        StringBuilder builder = new StringBuilder();
        int responseCode = urlConn.getResponseCode();
        builder.append(responseCode)
                .append(" ")
                .append(urlConn.getResponseMessage())
                .append("\n");

        InputStream res = urlConn.getInputStream();
        Scanner in = new Scanner(res);
        String responseStr = "";
        while(in.hasNextLine()) {
            String s = in.nextLine();
            responseStr+=s;
        }
        System.out.println(builder);
        System.out.println("responseStr: " + responseStr);
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (UnrecoverableKeyException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }
}

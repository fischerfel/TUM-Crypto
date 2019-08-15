try {

    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(new FileInputStream(
            "path_to_.jks"),
            "secret_of_jks".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);
    KeyManagerFactory kmf = KeyManagerFactory
            .getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, "secret_of_jks".toCharArray());
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    Socket s = ctx.getSocketFactory().createSocket("address_of_server", PORT);

    String jsonEx = "json text to send server";
    StringBuilder sb = new StringBuilder();
    sb.append(jsonEx.getBytes().length);
    sb.append("\r\n");
    sb.append(jsonEx);


    PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
    writer.println(sb.toString());
    BufferedReader in =  new BufferedReader(new InputStreamReader(s.getInputStream()));
    System.out.println(in.readLine());
    writer.flush();
} catch (Exception e) {
    e.printStackTrace();
}

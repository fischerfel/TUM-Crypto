public void testClientCertPEM() throws Exception {
    String requestURL = "https://mydomain/authtest";
    String pemPath = "C:/Users/myusername/Desktop/client.pem";

    HttpsURLConnection con;

    URL url = new URL(requestURL);
    con = (HttpsURLConnection) url.openConnection();
    con.setSSLSocketFactory(getSocketFactoryFromPEM(pemPath));
    con.setRequestMethod("GET");
    con.setDoInput(true);
    con.setDoOutput(false);  
    con.connect();

    String line;

    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

    while((line = reader.readLine()) != null) {
        System.out.println(line);
    }       

    reader.close();
    con.disconnect();
}

public SSLSocketFactory getSocketFactoryFromPEM(String pemPath) throws Exception {
    Security.addProvider(new BouncyCastleProvider());        
    SSLContext context = SSLContext.getInstance("TLS");

    PEMReader reader = new PEMReader(new FileReader(pemPath));
    X509Certificate cert = (X509Certificate) reader.readObject();        

    KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(null);
    keystore.setCertificateEntry("alias", cert);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(keystore, null);

    KeyManager[] km = kmf.getKeyManagers(); 

    context.init(km, null, null);

    return context.getSocketFactory();
} 

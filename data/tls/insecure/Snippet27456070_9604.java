    protected String tryLogin_2(String d1) throws CertificateException, FileNotFoundException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException 
{          

    // Load CAs from an InputStream
    // (could be from a resource or ByteArrayInputStream or ...)
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    // From https://www.washington.edu/itconnect/security/ca/load-der.crt


    InputStream caInput = new BufferedInputStream(this.getAssets().open("ssl.crt"));

    java.security.cert.Certificate ca;
    try {
        ca = cf.generateCertificate(caInput);
    } finally {
        caInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    // Create an SSLContext that uses our TrustManager
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);




    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    StrictMode.setThreadPolicy(policy); 

    String httpsURL = "https://URL.com";
    OutputStreamWriter request = null;
    DataInputStream response_2 = null; 
    String parameters = "1="+d1;   
    String response = null;     

    try
    {
    URL myurl = new URL(httpsURL);
    HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
    con.setSSLSocketFactory(context.getSocketFactory());
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-length", String.valueOf(query.length())); 
    con.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
    con.setDoOutput(true); 
    con.setDoInput(true); 

    request = new OutputStreamWriter(con.getOutputStream());
    request.write(parameters);
    request.flush();
    request.close();            
    String line = "";               
    InputStreamReader isr = new InputStreamReader(con.getInputStream());
    BufferedReader reader = new BufferedReader(isr);
    StringBuilder sb = new StringBuilder();
    while ((line = reader.readLine()) != null)
    {
    sb.append(line + "\n");
    }
    //Response from server after login process will be stored in response variable.                
    response = sb.toString();            
    isr.close();
    reader.close();
    }
    catch(IOException e)
    {
    response = "Error";     // Error
    }
    return response;


}

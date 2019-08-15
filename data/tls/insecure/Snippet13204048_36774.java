    URL url = new URL("https://abc.co.uk/someWS");
    String pKeyPassword = "xxxxxx";
    String xmlOutput = "someXML...";

    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

    //Load authentication certificate.
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
    KeyStore keyStore = KeyStore.getInstance("PKCS12");

    InputStream keyInput = new FileInputStream("/home/keystore.p12");
    keyStore.load(keyInput, pKeyPassword.toCharArray());
    keyInput.close();

    keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

    con.setSSLSocketFactory(context.getSocketFactory());

    // Tell the connection that we will be sending information
    con.setDoInput(true);
    con.setDoOutput(true);
    con.setRequestProperty("Content-Length", "" + xmlOutput.length());
    con.setRequestProperty("Content-Type", "text/xml; UTF-8");
    con.setRequestMethod("POST");

    con.connect();

    // Send the POST stream data
    DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
    outputStream.writeBytes(xmlOutput);

    // Read the response
    InputStream inputstream = con.getInputStream();
    InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
    BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

    // format response to a string
    String string = null;
    String response = "";
    while ((string = bufferedreader.readLine()) != null) {
      response += string;
    }
    con.disconnect();
    System.out.println(response);

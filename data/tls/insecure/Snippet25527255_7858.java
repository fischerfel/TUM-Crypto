public void setTestbedData(String path, String data)
    throws KeyStoreException, NoSuchAlgorithmException, CertificateException, 
    UnrecoverableKeyException, IOException
{
    HttpURLConnection con = null;
    con = (HttpURLConnection) ( new URL(Constants.BASE_URL + path)).openConnection();

    InputStream clientInput = new BufferedInputStream(new FileInputStream("/sdcard/Documents/user_cert.bks"));

    // load client certificate
    KeyStore keyStore = null;
    keyStore = KeyStore.getInstance("BKS");
    keyStore.load(clientInput, null);

    System.out.println("Loaded client certificates: " + keyStore.size());

    // initialize key manager factory with the read client certificate
    KeyManagerFactory keyManagerFactory = null;
    keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, null);


    SSLContext sc = SSLContext.getInstance("TLS");
    try {
        sc.init(keyManagerFactory.getKeyManagers(), null, null);
    } catch (KeyManagementException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

    if (con instanceof HttpsURLConnection) {
        ((HttpsURLConnection)con).setSSLSocketFactory(sc.getSocketFactory());
    }

    // If you invoke the method setDoOutput(true) on the URLConnection, it will always use the POST method.
    con.setRequestMethod("POST");
    con.setDoInput(true);
    con.setDoOutput(true);
    con.setRequestProperty("Accept", "application/json");
    con.setRequestProperty("Content-Type", "application/json");

    OutputStream outputStream = con.getOutputStream();
    outputStream.write(data.getBytes());
    outputStream.flush();

    InputStream _is;
    if (con.getResponseCode() /100 == 2) {
        _is = con.getInputStream();
    } else {
        _is = con.getErrorStream();

        String result = getStringFromInputStream(_is);
        Log.i("Error != 2xx", result);

        BufferedReader responseBuffer1 = new BufferedReader(new InputStreamReader((con.getErrorStream())));

        String output1;
        while ((output1 = responseBuffer1.readLine()) != null) {
            // ...
        }
    }        

    if (con.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : "
                                   + con.getResponseCode());
    }

    BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((con.getInputStream())));

    String output;
    while ((output = responseBuffer.readLine()) != null) {
        // ...
    }

    con.disconnect();
}

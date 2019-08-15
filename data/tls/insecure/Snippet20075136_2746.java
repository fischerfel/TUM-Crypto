private void downloadFromUrl(URL url, String localFilename) throws IOException {
    InputStream inputStream = null;
    FileOutputStream fileOutputStream = null;
    //System.setProperty("javax.net.ssl.keyStore", '/etc/keystore.ks');
    System.setProperty("javax.net.ssl.keyStorePassword", "passphrase");
    try 
    {

        char[] passphrase = "passphrase".toCharArray();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, passphrase);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keyStore);

        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);

        HostnameVerifier hostnameVerifier = new ClientHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        HttpsURLConnection urlSecureConn  = (HttpsURLConnection) url.openConnection();

        //URLConnection urlConn = url.openConnection();//connect
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setSSLSocketFactory(ssl.getSocketFactory());

        inputStream = urlSecureConn.getInputStream();               //get connection inputstream
        fileOutputStream = new FileOutputStream(localFilename);   //open outputstream to local file

        byte[] buffer = new byte[4096];              //declare 4KB buffer
        int len;

        //continue downloading and storing to local file while data is available
        while ((len = inputStream.read(buffer)) > 0) {  
            fileOutputStream.write(buffer, 0, len);
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    } 
    finally 
    {
        try 
        {
            if (inputStream != null) 
            {
                inputStream.close();
            }
        } 
        finally 
        {
            if (fileOutputStream != null) 
            {
                fileOutputStream.close();
            }
        }
    }
}

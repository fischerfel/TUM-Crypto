     CertificateFactory cf = CertificateFactory.getInstance("X.509");

     InputStream caInput = null;
     InputStream caInputKey = null;

    try {
         //get content files in input stream
        caInput = activity.getResources().openRawResource(R.raw.crtFile);
        caInputKey = activity.getResources().openRawResource(R.raw.keyFile);
    } catch (Exception e) {
        Log.d("DEBUG", e.toString());
    }


    Certificate ca = null;

     //IT WORKS
    try {
        ca = cf.generateCertificate(caInputKey);
        Log.d("ca=",((X509Certificate) ca).getSubjectDN());
    }catch (Exception e){

        Log.d("Exception", e.getCause().toString());
    } finally{
        caInput.close();
    }


    //CREATE A KEYSTORE TO STORE THE CERTIFICATE
    KeyStore keyStore = KeyStore.getInstance("PKCS12");

    //IT WORKS
    try {
        keyStore.setCertificateEntry("ca", ca);
    }catch (Exception e){
       Log.d("Exception", e.getCause().toString());
    }

   //CREATE A TrustManager to TRUST the keystore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    // Create an SSLContext that uses our TrustManager
    SSLContext sslcontext = SSLContext.getInstance("TLS");

    HttpsURLConnection urlConnection =           (HttpsURLConnection)url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("accept", "application/json");
        urlConnection.setConnectTimeout(2000);
        urlConnection.setSSLSocketFactory(sslcontext.getSocketFactory());
        urlConnection.setDoInput(true);

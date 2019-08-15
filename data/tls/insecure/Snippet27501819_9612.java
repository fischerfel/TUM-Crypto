        InputStream is = context.getResources().getAssets().open("CertificateFile.p12");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        BufferedInputStream bis = new BufferedInputStream(is);
        String password ="xxxxx";
        keyStore.load(bis, password.toCharArray()); // password is the PKCS#12 password. If there is no password, just pass null
        // Init SSL Context
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keyStore, password.toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, null, null);
        HttpsURLConnection urlConnection = null; 
        String strURL = "theUrlITryToHit";
        url = new URL(strURL);
        urlConnection = (HttpsURLConnection) url.openConnection();
        if(urlConnection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)urlConnection)
            .setSSLSocketFactory(sslContext.getSocketFactory());
        }
        urlConnection.setRequestMethod("GET");
        String basicAuth = "Basic " + Base64.encodeToString("pseudo:password".getBytes(), Base64.NO_WRAP);
        urlConnection.setRequestProperty ("Authorization", basicAuth);

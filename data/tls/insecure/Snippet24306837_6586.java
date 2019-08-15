        // get our trusted CA from resources
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        int caResId = context.getResources().getIdentifier("cert_ca", "raw", context.getPackageName());
        X509Certificate cert = (X509Certificate)certificateFactory.generateCertificate(context.getResources().openRawResource(caResId));
        String alias = cert.getSubjectX500Principal().getName();
        // create empty trust store with only our CA
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);
        trustStore.setCertificateEntry(alias, cert);
        // create TrustManagers based on our trust store
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(trustStore);
        TrustManager[] trustManagers = tmf.getTrustManagers();

        // get our client certificate from resources
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        String pass = context.getResources().getString(R.string.pass);
        int clientResId = context.getResources().getIdentifier("cert_client", "raw", context.getPackageName());
        keyStore.load(context.getResources().openRawResource(clientResId), pass.toCharArray());
        // create KeyManagers based on our key store 
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keyStore, pass.toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();

        // create SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, null);

        URL requestedUrl = new URL(url);
        HttpsURLConnection urlConnection = (HttpsURLConnection)requestedUrl.openConnection();
        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
        urlConnection.setRequestMethod("GET");
        urlConnection.setConnectTimeout(1500); // ?
        urlConnection.setReadTimeout(1500); // ?
        int responseCode = urlConnection.getResponseCode();
        String responseMessage = urlConnection.getResponseMessage();

        urlConnection.disconnect();

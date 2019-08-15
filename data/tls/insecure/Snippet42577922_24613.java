        try {
            // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt

            InputStream inn=getAssets().open("host.cert");

            Certificate ca;
            try {
                ca = cf.generateCertificate(inn);
                Log.i("TEST","ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                inn.close();
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

// Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL("https://192.168.56.1:8443/RestHTTPS/JavaCodeGeeks/AuthorService/authors/");
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            InputStream in = urlConnection.getInputStream();

            byte arr[]=new byte[in.available()];
            in.read(arr);

            String str=new String(arr);
            Log.i("TEST",str);

        }catch (Exception e){
            Log.e("TEST",e.toString());
            e.printStackTrace();
        }

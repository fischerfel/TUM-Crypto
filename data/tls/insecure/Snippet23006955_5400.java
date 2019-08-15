            TrustManagerFactory tmf1 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf1.init(keyStore);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, password);

            SSLContext sslContext = SSLContext.getInstance("TLS");


            sslContext.init(kmf.getKeyManagers(), tmf1.getTrustManagers(), null);


            // Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url =new URL("https://172.20.175.1/kani/");
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

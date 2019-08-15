        char[] passw = "password".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        ks.load(new FileInputStream ( "mykeystore" ), passw );

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passw);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        TrustManager[] tm = tmf.getTrustManagers();

        SSLContext sclx = SSLContext.getInstance("TLS");
        sclx.init( kmf.getKeyManagers(), tm, null);

        HostnameVerifier hv = new HostnameVerifier()
        {
            public boolean verify(String urlHostName, SSLSession session)
            {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost()))
                {
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };

        HttpsURLConnection.setDefaultSSLSocketFactory( sclx.getSocketFactory() );
        HttpsURLConnection.setDefaultHostnameVerifier(hv);

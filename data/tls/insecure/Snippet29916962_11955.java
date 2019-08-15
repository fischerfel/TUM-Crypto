URLConnection l_connection = null;
        // Create connection
        uzip=new UnZipData(mContext);
        l_url = new URL(serverurl);

        if ("https".equals(l_url.getProtocol())) {
            System.out.println("<<<<<<<<<<<<< Before TLS >>>>>>>>>>>>");
            sslcontext = SSLContext.getInstance("TLS");
            System.out.println("<<<<<<<<<<<<< After TLS >>>>>>>>>>>>");
            sslcontext.init(null,
                    new TrustManager[] { new CustomTrustManager()},
                    new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultHostnameVerifier(new CustomHostnameVerifier());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext
                    .getSocketFactory());

            l_connection = (HttpsURLConnection) l_url.openConnection();
            ((HttpsURLConnection) l_connection).setRequestMethod("POST");
        } else {
            l_connection = (HttpURLConnection) l_url.openConnection();
            ((HttpURLConnection) l_connection).setRequestMethod("POST");
        }
        /*System.setProperty("http.agent", "Android_Phone");*/


        l_connection.setConnectTimeout(10000);
        l_connection.setRequestProperty("Content-Language", "en-US");
        l_connection.setUseCaches(false);
        l_connection.setDoInput(true);
        l_connection.setDoOutput(true);
        System.out.println("<<<<<<<<<<<<< Before Connection >>>>>>>>>>>>");
        l_connection.connect();

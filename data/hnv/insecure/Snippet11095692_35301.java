public void setHttpsClient(String password) {

        try {
            KeyStore mycert = KeyStore.getInstance("pkcs12");

            byte[] pkcs12;

            //Load the PKCS file from database or file.
            pkcs12 = DataManager.getAuthP12Data();
            ByteArrayInputStream pkcs12BAIS = new ByteArrayInputStream(pkcs12);
            mycert.load(pkcs12BAIS, password.toCharArray());

            SSLSocketFactory sockfact = new SSLSocketFactory(mycert, null, null);

             sockfact.setHostnameVerifier(new StrictHostnameVerifier());

            // Done temporarily to accept all hosts
            //sockfact.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https", sockfact, 443));

            BasicHttpParams httpParameters = new BasicHttpParams();
            HttpProtocolParams.setUseExpectContinue(httpParameters, false);
            HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);

            HttpConnectionParams.setConnectionTimeout(httpParameters, _TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParameters, _TIMEOUT);

            ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
                    httpParameters, registry);
            cm.closeExpiredConnections();
            cm.closeIdleConnections(3000, TimeUnit.MILLISECONDS);

            _httpClient = new MyHttpClient(cm, httpParameters);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

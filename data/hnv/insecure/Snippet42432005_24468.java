public static RestServiceResponse sendHttpRequest(Context context, HttpRequestBase request, Map<String, String> headers){
        InputStream inputStream = null;
        ErrorMessage errorMessage = null;

        try{
            HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream caInput = context.getResources().openRawResource(R.raw.ebadminton);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);

            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", socketFactory, 443));

            HttpParams httpParams = new BasicHttpParams();

            SingleClientConnManager mgr = new SingleClientConnManager(httpParams, schemeRegistry);

            HttpClient httpclient = new DefaultHttpClient(mgr, httpParams);

            if(headers != null){
                for(Map.Entry<String, String> header : headers.entrySet()) {
                    request.setHeader(header.getKey(), header.getValue());
                }
            }

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
        }
        catch(Exception e){
            errorMessage = ErrorUtil.getNoInternetErrorMessage();
            Log.e(TAG, "Error in http connection " + e.toString());
        }

        RestServiceResponse restServiceResponse = setRestServiceResponse(inputStream, errorMessage);

        return restServiceResponse;
    }

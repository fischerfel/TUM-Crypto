public HttpClient getHttpClient() { 

 DefaultHttpClient client = null;

  ResponseCache.setDefault(null);

try {  





KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType()); 
            trustStore.load(null, null); 
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore); 
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 

            // Setting up parameters 
            HttpParams params = new BasicHttpParams(); 
            params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30); // default 30
            params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30)); // default 30
            //params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false); 
            HttpProtocolParams.setUseExpectContinue(params, false);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1); 
            HttpProtocolParams.setContentCharset(params, "UTF-8"); 

            // Setting timeout 
            HttpConnectionParams.setConnectionTimeout(params, 30000); 
            HttpConnectionParams.setSoTimeout(params, 30000); 

            // Registering schemes for both HTTP and HTTPS 
            SchemeRegistry registry = new SchemeRegistry(); 
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80)); 
            registry.register(new Scheme("https", sf, 443)); 

            // Creating thread safe client connection manager 
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry); 

            // Creating HTTP client 
            client  = new DefaultHttpClient(ccm, params); 

        } catch (Exception e) { 
            client = new DefaultHttpClient(); 
            Toast.makeText(AndroidLogin.this, "Problem with connection!", Toast.LENGTH_LONG).show(); 
        } 

        return client; 
    }

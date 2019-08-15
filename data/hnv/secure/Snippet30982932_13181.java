public static MyHttpClient createHttpClient(Context ctx) {    
        try {
            if(mgr == null){            
                SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();        
                sf.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);        

                httpParameters = new BasicHttpParams();        
                HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);        
                HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
                HttpConnectionParams.setConnectionTimeout(httpParameters, HOST_REACH_TIMEOUT); 
                HttpConnectionParams.setSoTimeout(httpParameters, HOST_REACH_TIMEOUT);

                SchemeRegistry registry = new SchemeRegistry();   
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));        
                registry.register(new Scheme("https", sf, 443));        

                mgr = new ThreadSafeClientConnManager(httpParameters, registry);
            }
            return new MyHttpClient(mgr, httpParameters);    
        } catch (Exception e) {        
            return new MyHttpClient(ctx);    
        }
    }

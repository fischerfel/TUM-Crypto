        SSLContext ctx = SSLContext.getInstance("SSL"); 
        ctx.init(null, new TrustManager[] { tm }, null); 

        SSLSocketFactory ssf = new SSLSocketFactory(ctx, 
                SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 
        ClientConnectionManager ccm = client.getConnectionManager(); 
        SchemeRegistry sr = ccm.getSchemeRegistry(); 
        sr.register(new Scheme("https4", 443, ssf)); 

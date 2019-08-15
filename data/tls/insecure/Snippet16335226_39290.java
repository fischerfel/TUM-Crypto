SSLSocketFactory sf=null ;
        SSLContext sslContext = null;
        StringWriter writer;
        try {
            sslContext = SSLContext.getInstance("TLS")  ;
            sslContext.init(null,null,null);
        } catch (NoSuchAlgorithmException e) {
            //<YourErrorHandling>
        }  catch (KeyManagementException e){
            //<YourErrorHandling>
        }

        try{
            sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        } catch(Exception e) {
            //<YourErrorHandling>

    }
        Scheme scheme = new Scheme("https",443,sf);
        httpClient.getConnectionManager().getSchemeRegistry().register(scheme);

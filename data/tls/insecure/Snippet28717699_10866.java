private DefaultHttpClient buildhttpClient() throws Exception {
            DefaultHttpClient httpclient = new DefaultHttpClient();

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, getTrustingManager(), new java.security.SecureRandom());

            SSLSocketFactory socketFactory = new SSLSocketFactory(sc);
            Scheme sch = new Scheme("https", 443, socketFactory);
            httpclient.getConnectionManager().getSchemeRegistry().register(sch);
            return httpclient;
        }

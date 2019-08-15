HttpClient httpClient = new DefaultHttpClient();
            // Secure Protocol implementation.
            SSLContext ctx = SSLContext.getInstance("SSL");
            // Implementation of a trust manager for X509 certificates
            X509TrustManager tm = new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)  throws java.security.cert.CertificateException {
                    // TODO Auto-generated method stub
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws java.security.cert.CertificateException {
                    // TODO Auto-generated method stub

                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);

            ClientConnectionManager ccm = httpClient.getConnectionManager();
            // register https protocol in httpclient's scheme registry
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));

            HttpGet httpget = new HttpGet("http://remoteserver.com/login.seam");
            HttpParams params = httpclient.getParams();

            params.setParameter("param1", "paramValue1");

            httpget.setParams(params);
            System.out.println("REQUEST:" + httpget.getURI());
            ResponseHandler responseHandler = new BasicResponseHandler();
            String responseBody;

            responseBody = httpclient.execute(httpget, responseHandler);

            System.out.println(responseBody);

        try {
            HttpPost httppost = new HttpPost(uri);
            HttpHost targetHost = new HttpHost(HOST_NAME, HOST_PORT, PROTOCOL);

            InputStreamEntity reqEntity = new InputStreamEntity(new ByteArrayInputStream(request), -1);
            String contentType = TSPConstants.CONST_TSA_CONTENT_TYPE_TSREQUEST;
            reqEntity.setContentType(contentType);
            reqEntity.setChunked(true);
            // It may be more appropriate to use FileEntity class in this particular
            // instance but we are using a more generic InputStreamEntity to demonstrate
            // the capability to stream out data from any arbitrary source
            //
            // FileEntity entity = new FileEntity(file, "binary/octet-stream");

            httppost.setEntity(reqEntity);

            //Authentication
            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                    new UsernamePasswordCredentials(id, password));
            // Create AuthCache instance
            AuthCache authCache = new BasicAuthCache();
            // Generate BASIC scheme object and add it to the local
            // auth cache
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(targetHost, basicAuth);

            // Add AuthCache to the execution context
            BasicHttpContext httpContext = new BasicHttpContext();
            httpContext.setAttribute(ClientContext.AUTH_CACHE, authCache);
            httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            //SSL
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException { }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException { }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

              Scheme sch = new Scheme("https", HOST_PORT, ssf);

              httpclient.getConnectionManager().getSchemeRegistry().register(sch);
            System.out.println("executing request " + httppost.getRequestLine());
            httpclient.execute(httppost, httpContext);
                    HttpResponse response = send(request);

            HttpEntity resEntity = response.getEntity();
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (resEntity != null) {
                System.out.println("Response content length: " + resEntity.getContentLength());
                System.out.println("Chunked?: " + resEntity.isChunked());
            }

            EntityUtils.consume(resEntity);
                resEntity.getContent()


        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();

        }

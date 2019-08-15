KeyStore trustStore = KeyStore.getInstance("JKS", "SUN");
            trustStore.load(SSLImplemetation.class.getResourceAsStream("C:/Program Files/Java/jdk1.7.0_79/jre/lib/security/cacerts"), "changeit".toCharArray());
            String alg = KeyManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory fac = TrustManagerFactory.getInstance(alg);
            fac.init(trustStore);


            KeyStore keystore = KeyStore.getInstance("JKS", "SUN");
            keystore.load(SSLImplemetation.class.getResourceAsStream("<dir path>/client-keystore.jks"), "test".toCharArra());
            String keyAlg = KeyManagerFactory.getDefaultAlgorithm();
            KeyManagerFactory keyFac = KeyManagerFactory.getInstance(keyAlg);
            keyFac.init(keystore, "test".toCharArray());

                SSLContext ctx = SSLContext.getInstance("TLS", "SunJSSE");
            ctx.init(keyFac.getKeyManagers(),fac.getTrustManagers(), new SecureRandom());

            SslContextedSecureProtocolSocketFactory secureProtocolSocketFactory = new SslContextedSecureProtocolSocketFactory(ctx);
            Protocol.registerProtocol("https", new Protocol("https", (ProtocolSocketFactory) secureProtocolSocketFactory, 8443));

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost("<rest service url>");

JSONObject obj = new JSONObject();
StringEntity params =new StringEntity(obj.toString());  

     request.addHeader("content-type", "application/json");
     request.setEntity(params);
     HttpResponse response = httpClient.execute(request);
     System.out.println(response.getStatusLine());

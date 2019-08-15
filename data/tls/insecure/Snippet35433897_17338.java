try {
        IEService port = null;
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[]{new TrustAllX509TrustManager()}, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String string, SSLSession ssls) {
                return true;
            }
        });

        EService service = new EService();
        port = service.getEServiceEndPoint();

        Map ctx = ((BindingProvider) port).getRequestContext();
        ctx.put("ws-security.username", "username");
        ctx.put("ws-security.password", "password");
        HTTPConduit httpConduit = (HTTPConduit) ClientProxy.getClient(port).getConduit();
        TLSClientParameters tlsCP = new TLSClientParameters();
        tlsCP.setDisableCNCheck(true);
        httpConduit.setTlsClientParameters(tlsCP);

        voucherResponse = port.registerVoucher(voucherRequest);
    } catch (Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();

    }

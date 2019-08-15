        String KEYSTOREPASS = "test";
        char[]ctpass = KEYSTOREPASS.toCharArray();
        KeyStore ks = KeyStore.getInstance("PKCS12");

        //Directly load cert from Resources
        //ks.load(ctx.getResources().openRawResource(R.raw.cayan_cert),kspass);

        //Or dynamically generate a cert and use it
        ipAddressInCN = MainApplication.getIPAddress();

        //Use the current IP Address to generate a cert that signed by hard coded CA, and add to keystore
        String CN = "CN=" + ipAddressInCN;
        ks.load(null, null);
        GenerateCSR.AddCertToKeyStore(ks, ctpass, CN);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, ctpass);

        SSLContext sc = SSLContext.getInstance("TLS");

        TrustManager[] tm = new TrustManager[]{new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                return new java.security.cert.X509Certificate[0];
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
                                           String authType) {
                System.out.println("abc");
                return;
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
                                           String authType) {

                return;
            }

        }};

        sc.init(kmf.getKeyManagers(), tm, null);
        server.makeSecure(sc.getServerSocketFactory(), null);

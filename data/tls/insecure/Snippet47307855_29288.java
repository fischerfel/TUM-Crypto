systemProps.setProperty("javax.net.ssl.keyStore", new File("Path of Certificate").getAbsolutePath());
            systemProps.setProperty("javax.net.ssl.keyStorePassword", "***");
            systemProps.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
            systemProps.setProperty("java.net.useSystemProxies", "true");


            String provider = System.getProperty("javax.net.ssl.keyStoreProvider");
            String keyStoreType = systemProps.getProperty("javax.net.ssl.keyStoreType");
            KeyStore ks = null;
            if (provider != null) {
                ks = KeyStore.getInstance(keyStoreType, provider);
            } else {
                ks = KeyStore.getInstance(keyStoreType);
            }

            InputStream ksis = null;
            String keystorePath = systemProps.getProperty("javax.net.ssl.keyStore");
            String keystorePassword = systemProps.getProperty("javax.net.ssl.keyStorePassword");
            if (keystorePath != null && !"NONE".equals(keystorePath)) {
                ksis = new FileInputStream(keystorePath);
            }

            try {
                ks.load(ksis, keystorePassword.toCharArray());
            } finally {
                 if (ksis != null) { ksis.close(); }
            }

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, keystorePassword.toCharArray());
            // Note that there is no property for the key password itself, which may be different.
            // We're using the keystore password too.

            SSLContext sc = SSLContext.getInstance("SSLv3");
            sc.init(kmf.getKeyManagers(), null, null);
            ((BindingProvider) ps).getRequestContext().put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory", sc.getSocketFactory());

            Response response = ps.submitRequest(request); service call. 
            System.out.println(response.toString());

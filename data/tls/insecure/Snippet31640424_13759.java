server = new WSServer(new InetSocketAddress(25517), activity.getApplicationContext());

            String STORETYPE = "BKS";
            String STOREPASSWORD = "titancast-androidapp";
            String KEYPASSWORD = "titancast-androidapp";

            WebSocketImpl.DEBUG = true;

            try {
                KeyStore ks = KeyStore.getInstance(STORETYPE);
                File kf = keystore;
                ks.load(new FileInputStream(kf), STOREPASSWORD.toCharArray());

                KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
                kmf.init(ks, KEYPASSWORD.toCharArray());

                TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                tmf.init(ks);

                SSLContext sslContext = null;
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

                wsf = new DefaultSSLWebSocketServerFactory(sslContext);

                server.setWebSocketFactory(wsf);

                server.start();
                TitanCastNotification.showToast("(hopefully) success", Toast.LENGTH_LONG);
            }catch(Exception e){
                e.printStackTrace();
            }

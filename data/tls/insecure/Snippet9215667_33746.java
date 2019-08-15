    KeyStore KeyStore1;

                try {
                    String keyStoreFile1 = "privatekey1";
                    String keyStoreType1 = "jks";
                    char[] keyStorePwd1 = "password".toCharArray();

                    keyStore1 = KeyStore.getInstance(keyStoreType1);
                    keyStore1.load(new FileInputStream(keyStoreFile1), keyStorePwd1);
                } catch (java.security.GeneralSecurityException thr) {
                    throw new IOException("Cannot load keystore (" + thr + ")");
                }

                KeyStore trustStore1;

                try {
                    String trustStoreFile1 = "publickey1";
                    String trustStoreType1 = "jks";
                    char[] trustStorePwd1 = "password".toCharArray();

                    trustStore1 = KeyStore.getInstance(trustStoreType1);
                    trustStore.load(new FileInputStream(trustStoreFile1), trustStorePwd1);
                } catch (java.security.GeneralSecurityException thr) {
                    throw new IOException("Cannot load truststore (" + thr + ")");
                }


                KeyStore keyStore2;

                try {
                    String keyStoreFile2 = "privatekey2";
                    String keyStoreType2 = "jks";
                    char[] keyStorePwd2 = "anotherpass".toCharArray();

                    keyStore2 = KeyStore.getInstance(key2StoreType);
                    keyStore2.load(new FileInputStream(keyStoreFile2), keyStorePwd2);
                } catch (java.security.GeneralSecurityException thr) {
                    throw new IOException("Cannot load keystore (" + thr + ")");
                }

                KeyStore trustStore2;

                try {
                    String trustStoreFile2 = "publickey2";
                    String trustStoreType2 = "jks";
                    char[] trustStorePwd2 = "anotherpass".toCharArray();

                    trustStore2 = KeyStore.getInstance(trustStoreType2);
                    trustStore2.load(new FileInputStream(trustStoreFile2), trustStorePwd2);
                } catch (java.security.GeneralSecurityException thr) {
                    throw new IOException("Cannot load truststore (" + thr + ")");
                }



                KeyManagerFactory kmfkey1 = KeyManagerFactory
.getInstance(KeyManagerFactory.getkey1Algorithm());

                kmfkey1.init(keyStore1, "password".toCharArray());

                TrustManagerFactory tmfkey1 =
                        TrustManagerFactory.getInstance(TrustManagerFactory.getkey1Algorithm());
                tmfkey1.init(trustStore1);


                SSLContext ctx = SSLContext.getInstance("SSL");
                ctx.init(kmfkey1.getKeyManagers(), tmfkey1.getTrustManagers(), null);


                KeyManagerFactory kmfkey2 =  KeyManagerFactory.
getInstance(KeyManagerFactory.getkey1Algorithm());

                kmfkey2.init(keyStore2, "password".toCharArray());

                TrustManagerFactory tmfkey2 =  TrustManagerFactory
.getInstance(TrustManagerFactory.getkey1Algorithm());

                tmfkey2.init(trustStore2);


                SSLContext ctxkey2 = SSLContext.getInstance("SSL");
                ctxkey2.init(kmfkey2.getKeyManagers(), tmfkey2.getTrustManagers(), null);

                SSLServerSocketFactory sslSrvSockFact = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

                serverSocket = sslSrvSockFact.createServerSocket(port);

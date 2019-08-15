public SSLServerSocket createServerSocket() {
        try {
            KeyStore keystore = KeyStore.getInstance("JKS");
            FileInputStream keystoreInputStream = new FileInputStream(KEYSTORE_NAME);
            keystore.load(keystoreInputStream, STORES_PASSWORD.toCharArray());
            KeyStore truststore = KeyStore.getInstance("JKS");
            FileInputStream truststoreInputStream = new FileInputStream(TRUSTSTORE_NAME);
            truststore.load(truststoreInputStream, STORES_PASSWORD.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
            trustManagerFactory.init(truststore);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
            keyManagerFactory.init(keystore, STORES_PASSWORD.toCharArray());
            X509ExtendedKeyManager x509KeyManager = null;
            for (KeyManager keyManager : keyManagerFactory.getKeyManagers()) {
                if (keyManager instanceof X509KeyManager) {
                    x509KeyManager = (X509ExtendedKeyManager) keyManager;
                    break;
                }
            }
            if (x509KeyManager == null) {
                debug("Searched for x509 key managers but found none.");
                throw new NullPointerException();
            }
            X509ExtendedTrustManager x509TrustManager = null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509ExtendedTrustManager) trustManager;
                }
            }
            if (x509TrustManager == null) {
                debug("Searched for x509 trust managers but found none.");
                throw new NullPointerException();
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManager[] keyManagers = { x509KeyManager };
            TrustManager[] trustManagers = { x509TrustManager };
            sslContext.init(keyManagers, trustManagers, null);
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT);
            serverSocket.setNeedClientAuth(true);
            /* Force TLS 1.0, this will disable i.e SSL2 which is insecure. */
            //serverSocket.setEnabledProtocols(new String[] { "TLSv1" });
            return serverSocket;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

while (true) {
    try {
        debug("Listening for incoming connections...");
        SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
        // Handle connection in a separate thread.
        ListenerThread thread = new ListenerThread(clientSocket);
        thread.start();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

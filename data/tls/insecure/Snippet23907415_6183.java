public SSLSocket getSocket(String ip) throws IOException {
        TrustManagerFactory trustManagerFactory = null;
        try {
            KeyStore trustStore = KeyStore.getInstance("BKS");  
            trustManagerFactory = TrustManagerFactory  
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());  
            App app = new App();
            InputStream trustStoreStream = app.getContext().getResources()  
                .openRawResource(R.raw.truststore);  
            trustStore.load(trustStoreStream, "mypassword".toCharArray());  
            trustManagerFactory.init(trustStore);

            outerSSLContext = SSLContext.getInstance("TLS");  
            outerSSLContext.init(null, trustManagerFactory.getTrustManagers(), null); 

            SSLSocketFactory outerSSLSocketFactory = (SSLSocketFactory) outerSSLContext  
                    .getSocketFactory();  
                 SSLSocket outerSocket = (SSLSocket) outerSSLSocketFactory.createSocket();
                 outerSocket.setSoTimeout(Constants.SOCKET_READ_TIMEOUT);

                 InetAddress serverAddr = InetAddress.getByName(ip);
                InetSocketAddress address = new InetSocketAddress(serverAddr, Integer.parseInt(serverPort));
                outerSocket.connect(address, Constants.SOCKET_CONNECTION_TIMEOUT);

                 socket = outerSocket;
                 socket.startHandshake();
         } catch (KeyStoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (CertificateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        return socket;
    }

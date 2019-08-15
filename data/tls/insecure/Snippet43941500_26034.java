        char[] password = "Password".toCharArray();

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(getClass().getResourceAsStream("/security/keystore.jks"), password);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(7826);
        System.out.println("Accepting connections now...");

        while(true){
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            socket.setUseClientMode(false);
        socket.addHandshakeCompletedListener(handshakeCompletedEvent -> {
            try{
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                while(running){
                    if(!socket.isConnected() || socket.isClosed()){
                        disconnect();
                        return;
                    }
                    for(Iterator<String> pendingIterator = pendingMessages.iterator(); pendingIterator.hasNext();){
                        String message = pendingIterator.next();
                        pendingIterator.remove();
                        writer.println(message); writer.flush();
                    }

                    //Auto decrypt when message arrives, but no thread blocking
                    if(reader.ready()){
                        String line = reader.readLine();
                        System.out.println(line);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        socket.startHandshake();
        }

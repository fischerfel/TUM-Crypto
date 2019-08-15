        char[] password = "Password".toCharArray();

        KeyStore trustStore = KeyStore.getInstance("BKS");
        trustStore.load(activity.getResources().openRawResource(R.raw.truststore), password);
        Log.i("Update", "Loaded trust certificate");

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        Log.i("Update", "Loaded trustManagerFactory");

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(activity.getResources().openRawResource(R.raw.keystore), password);
        Log.i("Update", "Loaded key certificate");

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        Log.i("Update", "Loaded keyManagerFactory");

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        Log.i("Update", "Loaded SSLContext");

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        final SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(InetAddress.getByName(host), port);
        Log.i("Update", "Loaded SSL 

        socket.addHandshakeCompletedListener(new HandshakeCompletedListener(){
            @Override
            public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent){
                try{
                    Log.i("Update", "Did handshake");

                    writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    Log.i("Update", "Writer");
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Log.i("Update", "Reader");

                    while(isRunning){
                        try{
                            writer.println("Test");
                            writer.flush();
                            Log.i("DATA", "DATA SENT");
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    writer.close();
                    reader.close();
                    socket.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        socket.startHandshake();

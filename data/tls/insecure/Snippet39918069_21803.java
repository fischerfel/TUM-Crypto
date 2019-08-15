protected void secure_transfer(String ip, int port, int fileno) throws IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, KeyManagementException {

        String passphrase = "test123";

        KeyStore ks = KeyStore.getInstance("BKS");
        InputStream keyin = getResources().openRawResource(R.raw.server_keystore);
        ks.load(keyin, "test123".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        KeyStore clientKeyStore = KeyStore.getInstance("BKS");
        clientKeyStore.load(getResources().openRawResource(R.raw.client_keystore), passphrase.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientKeyStore, passphrase.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket)
                socketFactory.createSocket(new Socket(ip, port), ip, port, false);
        socket.startHandshake();
        try {

            // sendfile
            final InputStream is;
            switch (fileno){
                case 0:
                    is = getResources().openRawResource(R.raw.test1mb);
                    break;
                case 1:
                    is = getResources().openRawResource(R.raw.test10mb);
                    break;
                case 2:
                    is = getResources().openRawResource(R.raw.test100mb);
                    break;
                default: is = getResources().openRawResource(R.raw.test1mb);
                    break;
            }
            BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
            int buffer_size = Integer.parseInt(buffersize.getText().toString());
            byte buffer[] = new byte[buffer_size];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                os.write(buffer, 0, count);
            }
            os.flush();
            socket.close();
        } catch (UnknownHostException e) {
            log.append("Error \n" + e);
        } catch (IOException e) {
            log.append("Error \n" + e);

        }
    }

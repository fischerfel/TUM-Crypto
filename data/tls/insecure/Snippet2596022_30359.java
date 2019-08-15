try {
            System.setProperty("javax.net.debug","all");
            /*
             * Set up a key manager for client authentication
             * if asked by the server.  Use the implementation's
             * default TrustStore and secureRandom routines.
             */
            SSLSocketFactory factory = null;
            try {
            SSLContext ctx;
            KeyManagerFactory kmf;
            KeyStore ks;
            char[] passphrase = "importkey".toCharArray();

            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            ks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream("keystore.jks"), passphrase);

            kmf.init(ks, passphrase);
            ctx.init(kmf.getKeyManagers(), null, null);

            factory = ctx.getSocketFactory();
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }

            SSLSocket socket = (SSLSocket)factory.createSocket("server ip", 9999);

            /*
             * send http request
             *
             * See SSLSocketClient.java for more information about why
             * there is a forced handshake here when using PrintWriters.
             */
            SSLSession session = socket.getSession();

            [build query]

            byte[] buff = query.toWire();

            out.write(buff);
            out.flush();

            InputStream input = socket.getInputStream();

            int readBytes = -1;
            int randomLength = 1024;
            byte[] buffer  = new byte[randomLength];
            while((readBytes = input.read(buffer, 0, randomLength)) != -1) {
                LOG.debug("Read: " + new String(buffer));
            }
            input.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

            char certPass[] = "***";
            char certAliaMainPass[] = "***";;
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(certPath), certPass);
            KeyManagerFactory keyManagerFactory =         KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, certAliaMainPass);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(iPort);

            String[] protocols = sslServerSocket.getEnabledProtocols();
            Set<String> set = new HashSet<String>();
            for (String s : protocols) {
                if (s.equals("SSLv3")) {
                    continue;
                }
                set.add(s);
            }
            sslServerSocket.setEnabledProtocols(set.toArray(new String[0]));

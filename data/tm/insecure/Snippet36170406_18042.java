        ks.load(new FileInputStream("ClientKeyStore"), keyPassPhrase);
        provider();
        final KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509"); // this line is problem

        // SunX509 : supporting only: [TLSv1, TLSv1.1, TLSv1.2]
        kmf.init(ks, keyPassPhrase);

        sc.init(kmf.getKeyManagers(), new TrustManager[] {
                new X509TrustManager(){
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                            throws CertificateException {

                    }
                    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                            throws CertificateException {
                    }
                }
        },new SecureRandom());
        SSLSocketFactory factory = sc.getSocketFactory();
        SSLSocket socket=null;
        try{
            //socket = (SSLSocket) factory.createSocket("XXXX",xxxx);/
            socket = (SSLSocket) factory.createSocket(ipAddress, Integer.parseInt(ports[portIndex]));

            //convert to array
            String[] cipherSelectedArray;
            if(isSupported == 1 ) {
                cipherSelectedArray = new String[] {msupportedcipherList.get(cipherIndex).trim()};
            }
            else {
                cipherSelectedArray = new String[] {mnotSupportedcipherList.get(cipherIndex).trim()};
            }

            String []mselectedSSLOrTLSVersionArrray = new String[] {mselectedSSLOrTLSVersion};   // if passing these --> getting connection timeout

            socket.setEnabledProtocols(mselectedSSLOrTLSVersionArrray);
            socket.setEnabledCipherSuites(cipherSelectedArray);
            for(int i = 0; i<cipherSelectedArray.length ; i++) {
                //System.out.println("ciphers are :" +  cipherSelectedArray[i]);
            }


            socket.setSoTimeout(15000);

            socket.addHandshakeCompletedListener(new HandshakeCompletedListener() {

                @Override
                public void handshakeCompleted(HandshakeCompletedEvent event) {
                    ////System.out.println("completed");

                }
            });



            socket.startHandshake(); //handshake                                            as "SunX509" does not support SSL. I need to create above one. Can someone help.   And also with "SunX509" i am getting                                              java.lang.IllegalArgumentException: Cannot support TLS_RSA_WITH_AES_256_CBC_SHA with currently installed providers problem with some ciphers. please help

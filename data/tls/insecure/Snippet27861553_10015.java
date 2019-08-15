    SSLContext sc;
SSLSocket sslsock;
Socket sock;

// Constructor
RfbProto(String h, int p) throws IOException{
        host = h;
        port = p;

        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {  
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
                        return null;  
                    }  
                    public void checkClientTrusted(X509Certificate[] certs, String authType){}
                    public void checkServerTrusted(X509Certificate[] certs, String authType){}
                }
        };  

        try {
            sc = SSLContext.getInstance("SSL");  
            sc.init(null, trustAllCerts, new java.security.SecureRandom());  
        } catch (Exception e) { }

        sock = new Socket();
        sock.connect(new InetSocketAddress(host, port), 5000);
        sock.setSoTimeout(10000);

        SSLSocketFactory factory = (SSLSocketFactory)sc.getSocketFactory();
        sslsock = (SSLSocket)factory.createSocket(sock, null, 0, false);

        is = new DataInputStream(new BufferedInputStream(sock.getInputStream(), 16384));
        os = sock.getOutputStream();    

        timing = false;
        timeWaitedIn100us = 5;
        timedKbits = 0;
    }

    public void enableSSL() throws IOException{

        this.sslEnabled = true;

        final SSLSocket fSock = sslsock;
        sslsock.addHandshakeCompletedListener(new HandshakeCompletedListener() {            
            @Override
            public void handshakeCompleted(HandshakeCompletedEvent event) {
                try {
                    Certificate[] peerCertificates = event.getPeerCertificates();
                    if (peerCertificates.length > 0) {
                        serverCertificate = (X509Certificate)peerCertificates[0];
                    }       
                    is = new DataInputStream(new BufferedInputStream(fSock.getInputStream(), 16384));
                    os = fSock.getOutputStream();

                } catch (SSLPeerUnverifiedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            fSock.startHandshake();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

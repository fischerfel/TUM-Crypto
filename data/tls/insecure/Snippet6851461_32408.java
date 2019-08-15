    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, trustAllCerts, new SecureRandom());
    s = (SSLSocket)sslContext.getSocketFactory().createSocket();
    s.connect(new InetSocketAddress(host, port), timeout);
    s.setSoTimeout(0);
    ((SSLSocket)s).startHandshake();

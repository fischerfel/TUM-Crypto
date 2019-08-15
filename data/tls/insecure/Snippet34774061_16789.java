    SSLContext sc = SSLContext.getInstance("TLS");
    (...)
    SSLServerSocketFactory ssf = sc.getServerSocketFactory();
    SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(portNumber);
    while (listening) {
        SSLSocket c = (SSLSocket) s.accept();
        log.info("Serving");
        new SimpleSSLServerSocketThread(c).start();
    }

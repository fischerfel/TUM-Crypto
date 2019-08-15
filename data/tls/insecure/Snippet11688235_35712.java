SSLSocketFactory socketFactory = SSLContext.getInstance("TLS").getSocketFactory();
SSLSocket socket = (SSLSocket) socketFactory.createSocket(InetAddress.getByName(hostname), port);
socket.setEnabledCipherSuites(new String[] {
        "SSL_RSA_WITH_RC4_128_MD5",
        "SSL_RSA_WITH_RC4_128_SHA",
        "TLS_RSA_WITH_AES_128_CBC_SHA",
        "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
        "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
        "SSL_RSA_WITH_3DES_EDE_CBC_SHA",
        "SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
        "SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA",
        "SSL_RSA_WITH_DES_CBC_SHA",
        "SSL_DHE_RSA_WITH_DES_CBC_SHA",
        "SSL_DHE_DSS_WITH_DES_CBC_SHA",
        "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
        "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
        "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
        "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA",
        "TLS_EMPTY_RENEGOTIATION_INFO_SCSV"});

socket.startHandshake();

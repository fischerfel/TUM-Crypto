    System.setProperty("javax.net.ssl.keyStore", "./keystore");
    System.setProperty("javax.net.ssl.keyStorePassword", "nopassword");
    java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");

// Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }
    };

    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("TLSv1.2");
    sc.init(null, trustAllCerts, new SecureRandom());
    SSLServerSocket ssl = (SSLServerSocket) sc.getServerSocketFactory().createServerSocket(
            DownloadFilelist.PORT);
    // Got rid of:
    //ssl.setEnabledCipherSuites(sc.getServerSocketFactory().getSupportedCipherSuites());
    ssl.setEnabledProtocols(new String[] {"TLSv1", "TLSv1.1", "TLSv1.2", "SSLv3"});

    // System.out.println(Arrays.toString(ssl.getEnabledCipherSuites()));

    s = ssl;
    // s = new ServerSocket(DownloadFilelist.PORT);
    s.setSoTimeout(TIMEOUT);

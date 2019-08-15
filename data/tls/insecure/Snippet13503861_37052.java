    // configure the SSLContext with a TrustManager
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(new KeyManager[0], 
             new TrustManager[] {new DefaultTrustManager()}, 
             new SecureRandom());
    SSLContext.setDefault(ctx);

    URL url = new URL(urlString); // https://abc.myhost.com
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    conn.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                System.out.println("verify:" + arg0);
                return true;
            }
        });

    System.out.println("HTTP status: " + conn.getResponseCode());
    Certificate[] certs = conn.getServerCertificates();
    int c=0;
    for (Certificate cert : certs){
        String t = cert.getType();
        System.out.println(String.format("\ncert[%d]: %s",c,t));
        c++;
        if (pi.verbose) {
            System.out.println(cert);
        }
        else if (cert instanceof X509Certificate) {
            X509Certificate x509cert = (X509Certificate) cert;
            System.out.println(x509cert.getSubjectDN().getName());
        }
    }

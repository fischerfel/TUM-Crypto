private void installSSLContextPKCS11() throws Exception {
    PKCS11Provider provider = new PKCS11Provider("/usr/lib/opensc-pkcs11.so.BAK");
    Security.addProvider(provider);

    System.out.println("loading truststore");
    KeyStore tks = KeyStore.getInstance(KeyStore.getDefaultType());
    tks.load(new FileInputStream("/home/dan/Dokumente/Zertifikate/store"), "xxx".toCharArray());                   // load truststore

    System.out.println("loading keystore");
    KeyStore iks = KeyStore.getInstance("PKCS11", provider);  //works fine. he asks for a right pin - cancels when pin is wrong
    iks.load(null, "zzz".toCharArray());                                                                                                         // load private keystore

    System.out.println("init truststore");
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());            // init truststore
    tmf.init(tks);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");  // here is the problem. It seems that the pin is ignored. and if i overgive the provider (like KeyStore.getInstance-Method)i get an NoSuchAlgorithmException (for stacktrace see below)
    kmf.init(null, "834950".toCharArray());  //The debugger shows in kmf.getKeyManagers()-Array no priv. Key or anything. It contains nothing but an empty hashmap (or something like this) with p12 it contains the priv. key and the certificate from the smart card

    System.out.println("setting sslcontext");
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    SSLContext.setDefault(ctx);

    System.out.println("doing handshake");
    final SSLSocketFactory factory = ctx.getSocketFactory();
    final SSLSocket socket = (SSLSocket) factory.createSocket("download.uv.ruhr-uni-bochum.de", 443);
    socket.setUseClientMode(true);
    socket.startHandshake();   // here i try to do the handshake. it works with a p12-keystore... like ahead. with pkcs11 i get an SSLHandshakeException (Received fatal alert: handshake_failure)
    System.out.println("done");
}

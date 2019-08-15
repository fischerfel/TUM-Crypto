    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = new BufferedInputStream(new FileInputStream("myCer.cer"));
    Certificate ca;
    try {
        ca = cf.generateCertificate(caInput);
        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
    } finally {
        caInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    // Create an SSLContext that uses our TrustManager
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);

    SSLSocket sock = (SSLSocket)context.getSocketFactory().createSocket("...",21000); //"...": here I ignore the host name. The address and port is right.
    sock.setUseClientMode(true);
    if(sock.isConnected()) {
        System.out.println("Connected...");
    }
    else
    {
        System.out.println("Connect Fails...");
    }

    Login.pbLogin login = Login.pbLogin.newBuilder().setUserID("dbs")
                                    .setPassword("abcd1234")
                                    .setNewPassword("")
                                    .setClientVersion("1.0.0.0")
                                    .setRestarted(true)
                                    .build();


    OutputStream outputStream =sock.getOutputStream();
    byte[] b1=login.getClass().getSimpleName().getBytes("UTF-8");
    byte[] b2=login.toByteArray();

    byte[] bytes = ByteBuffer.allocate(4).putInt(b1.length + b2.length).array();
    outputStream.write(bytes);
    outputStream.write(b1);  //login.getClass().getSimpleName().getBytes("UTF-8")
    outputStream.write(b2);  //login.toByteArray()
    outputStream.flush();

    byte[] content = new byte[100];
    int bytesRead = -1;
    InputStream inputStream = sock.getInputStream();
    String str;

    while(( bytesRead = inputStream.read( content) ) != -1){
        System.out.println("OK ,receive.....");
       // str = new String(Arrays.copyOfRange(content,0,bytesRead), StandardCharsets.UTF_8);
        //System.out.println(str);
    }

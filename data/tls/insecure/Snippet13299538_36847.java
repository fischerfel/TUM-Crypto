    SSLSocketFactory factory = null;
    try {
        SSLContext ctx;
        KeyManagerFactory kmf;
        KeyStore ks;
        char[] passphrase = "android".toCharArray();

        ctx = SSLContext.getInstance("TLS");
        kmf = KeyManagerFactory.getInstance("SunX509");
        ks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream("keystore.ImportKey"), passphrase);

        kmf.init(ks, passphrase);
        ctx.init(kmf.getKeyManagers(), null, null);

        factory = ctx.getSocketFactory();
    } catch (Exception e) {
        e.printStackTrace();
    }

    SSLSocket socket;

    try {
        socket = (SSLSocket) factory.createSocket("server", 10443);

        PrintWriter out;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream())));

        out.println("Hello from SSL client!");
        out.println();
        out.flush();

        out.close();
        socket.close();

    } catch (IOException e) {
        e.printStackTrace();
    }

    String sAXLSOAPRequest = "...";
    byte[] bArray = null; // buffer for reading response from
    Socket socket = null; // socket to AXL server
    OutputStream out = null; // output stream to server
    InputStream in = null; // input stream from server

    X509TrustManager xtm = new MyTrustManager();
    TrustManager[] mytm = { xtm };
    SSLContext ctx = SSLContext.getInstance("SSL");
    ctx.init(null, mytm, null);
    SSLSocketFactory sslFact = (SSLSocketFactory) ctx.getSocketFactory();

    socket = (SSLSocket) sslFact.createSocket("192.168.1.100", Integer.parseInt("8443"));
    in = socket.getInputStream();
    // send the request to the server
    // read the response from the server
    StringBuffer sb = new StringBuffer(2048);
    bArray = new byte[2048];
    int ch = 0;
    int sum = 0;
    out = socket.getOutputStream();
    out.write(sAXLSOAPRequest.getBytes());

    while ((ch = in.read(bArray)) != -1) {
        sum += ch;
        sb.append(new String(bArray, 0, ch));
    }
    socket.close();
    // output the response to the standard output
    System.out.println(sb.toString());

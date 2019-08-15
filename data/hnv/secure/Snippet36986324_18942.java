public String sendByHTTPSNaive(String destinationURI,
                                String msgBodyContent) {

    String result = "no response";
    try {
        URL url = new URL(destinationURI);
        // URLConnection con = url.openConnection();

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        // specify that we will send output and accept input
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setConnectTimeout(20000); // long timeout, but not infinite
        con.setReadTimeout(20000);
        con.setUseCaches(false);
        con.setDefaultUseCaches(false);

        NoopHostnameVerifier HOSTNAME_VERIFIER = new NoopHostnameVerifier();
        TrustManager[] TRUST_MANAGER = { new NaiveTrustManager() };
        if (con instanceof HttpsURLConnection) {

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(new KeyManager[0], TRUST_MANAGER,
                                            new SecureRandom());
            SSLSocketFactory socketFactory = context.getSocketFactory();
            ((HttpsURLConnection) con).setSSLSocketFactory(socketFactory);
            ((HttpsURLConnection) con).setHostnameVerifier(HOSTNAME_VERIFIER);
        }

        // tell the web server what we are sending
        con.setRequestProperty("Content-Type", "text/xml");
        OutputStreamWriter writer = new OutputStreamWriter(
                                        con.getOutputStream());
        writer.write(msgBodyContent);
        writer.flush();
        writer.close();

        // reading the response
        InputStreamReader reader = new InputStreamReader(con.getInputStream());
        StringBuilder buf = new StringBuilder();

        char[] cbuf = new char[2048];
        int num;

        while (-1 != (num = reader.read(cbuf))) {
            buf.append(cbuf, 0, num);
        }

        result = buf.toString();
    } catch (Throwable t) {
        t.printStackTrace(System.out);
    }
    return result;
}

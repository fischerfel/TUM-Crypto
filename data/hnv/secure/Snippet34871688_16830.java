public String callService(String _data, String _signature) {
    SSLContext sslcontext = null;
            String response = null;
    try {
        sslcontext = SSLContext.getInstance("SSL");

        sslcontext.init(new KeyManager[0],
                new TrustManager[] { new DummyTrustManager() },
                new SecureRandom());
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace(System.err);
    } catch (KeyManagementException e) {
        e.printStackTrace(System.err);
    }

    SSLSocketFactory factory = sslcontext.getSocketFactory();

    String data = _data;
    String signature = _signature;
    String urlParameters = "data=";
    try {

        urlParameters = urlParameters + URLEncoder.encode(data, "UTF-8")
                + "&signature=" + URLEncoder.encode(signature, "UTF-8");
    } catch (Exception e) {

        e.printStackTrace();

    }

    try {
        URL url;
        HttpsURLConnection connection;
        InputStream is = null;

        url = new URL("https://**.**.**.**/TLS/Inquiry");

        connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        connection.setRequestProperty("Content-Length",
                "" + Integer.toString(urlParameters.getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.setSSLSocketFactory(factory);
        connection.setHostnameVerifier(new DummyHostnameVerifier());

        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        osw.write(urlParameters);
        osw.flush();
        osw.close();

        is = connection.getInputStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(is));

        response = in.readLine();

        System.out.println("Output " + response);
        is.close();
        in.close();

    } catch(ConnectException connExp) {
                connExp.printStackTrace();
            } catch (Exception e) {
        e.printStackTrace();
    }

            return response;        
}

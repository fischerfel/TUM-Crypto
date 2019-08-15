HttpsURLConnection conn = null;
    RESTResponse response = null;
    int status = -1;
    try {
        List<NameValuePair> params = request.getParameters();
        String uri = request.getRequestUri().toString();

        if (request.getMethod() == RESTMethods.GET) {
            if (params != null) {
                uri += "?";
                boolean first_param = true;

                for (NameValuePair p : params) {
                    if (first_param)
                        first_param = false;
                    else
                        uri += "&";

                    uri += p.getName() + "="
                            + URLEncoder.encode(p.getValue(), "UTF-8");
                }
            }
        }

        Security.addProvider(new BouncyCastleProvider());

        char[] passphrase = "mypass".toCharArray();
        try {
            KeyStore ksTrust = KeyStore.getInstance("BKS");
            ksTrust.load(context.getResources().openRawResource(R.raw.mystore),passphrase);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

            tmf.init(ksTrust);

            // Create a SSLContext with the certificate
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
            Log.v(TAG,"URI = " + uri);
            URL url = new URL(uri);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setSSLSocketFactory(sslContext.getSocketFactory());
            conn.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        if (request.getHeaders() != null) {
            for (String header : request.getHeaders().keySet()) {
                for (String value : request.getHeaders().get(header)) {
                    conn.addRequestProperty(header, value);
                }
            }
        }

        switch (request.getMethod()) {
        case GET:
            conn.setDoOutput(false);
            break;
        case POST:
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Connection", "close"); // disables connection reuse but getting ERROR


            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(getQuery(params));
            wr.flush();
            wr.close();
        default:
            break;
        }

        try {               


            conn.connect();
            status = conn.getResponseCode(); //Receiving EOFException when Connection close not set
        } catch (IOException ex1) {
            //check if it's eof, if yes retrieve code again

                ex1.printStackTrace();
                // handle exception

        }

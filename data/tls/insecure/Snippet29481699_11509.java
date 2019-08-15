public static JSONObject get_response(List<Parameter> params,
        final String address) throws IOException, JSONException, LeadException {
    // String body = "param1=" + URLEncoder.encode( "value1", "UTF-8" ) +
    // "&" +
    // "param2=" + URLEncoder.encode( "value2", "UTF-8" );

    StringBuilder body = new StringBuilder();
    for (Parameter pa : params) {
        body.append(pa.getKey() + "="
                + URLEncoder.encode(pa.getValue(), "UTF-8") + "&");
    }
    try {

        URL url = new URL(address);

        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        if (connection instanceof HttpsURLConnection) {
            KeyStore trusted = KeyStore.getInstance("BKS");

            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream in = Datastore.getActiv().getResources()
                    .openRawResource(R.raw.truststore);
            // Initialize the keystore with the provided trusted
            // certificates
            // Also provide the password of the keystore
            trusted.load(in, "lead".toCharArray());

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);



            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);


            ((HttpsURLConnection) connection)
                    .setSSLSocketFactory(context.getSocketFactory());

        }
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length",
                String.valueOf(body.length()));
        OutputStreamWriter writer = new OutputStreamWriter(
                connection.getOutputStream());

        writer.write(body.toString());
        writer.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));

        StringBuilder build = new StringBuilder();
        for (String line; (line = reader.readLine()) != null;) {
            build.append(line);
        }

        writer.close();
        reader.close();

        return new JSONObject(build.toString());

    } catch (UnknownHostException | FileNotFoundException
            | ConnectException e) {
        Datastore.getActiv().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(
                        Datastore.getActiv(),
                        "Konnte keine Verbindung zum Server ( "
                                + address
                                + " ) herstellen.\nBitte überprüfen Sie ihre Netzwerkverbindung und / oder\nkontaktieren Sie einen Administrator",
                        Toast.LENGTH_LONG).show();
                ;

            }

        });
        throw e;
    } catch (KeyManagementException|KeyStoreException|NoSuchAlgorithmException|CertificateException e) {
        MessageDialog.showMessageDialog(Datastore.getActiv(), "Es ist ein kritischer Fehler beim Herstellen der sicheren Verbindung aufgetreten");
        e.printStackTrace();
        throw new LeadException(LeadException.Errorcode.UNKNOWN);
    } 

}

        System.setProperty("http.keepAlive", "false");
        HttpsURLConnection
                .setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname,
                            SSLSession session) {
                        // TODO Auto-generated method stub
                        return false;


        char[] passwKey = "pass".toCharArray();
        KeyStore ts = KeyStore.getInstance("PKCS12");

        InputStream in = getResources().openRawResource(
                R.raw.CertificateFile);
        ts.load(in, passwKey);
        KeyManagerFactory tmf = KeyManagerFactory
                .getInstance("X.509");
        tmf.init(ts, passwKey);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(tmf.getKeyManagers(),
                new X509TrustManager[] { new MyX509TrustManager(in,
                        "mobile".toCharArray()) }, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());

        URL url = new URL("https://url");
        HttpsURLConnection connection = (HttpsURLConnection) url
                .openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "params");
        connection.setRequestProperty("AppName", "params");
        connection.setRequestProperty("AppID",
                "params");

        BufferedReader bf = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String inputLine;

        while ((inputLine = bf.readLine()) != null) {
            txtMain.append("response " + inputLine + "\n");
            Log.d("@: ", inputLine);
        }
        in.close();

    } catch (Exception e) { // should never happen
        e.printStackTrace();
    }

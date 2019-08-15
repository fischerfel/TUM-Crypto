try{

        System.setProperty("http.keepAlive", "false");
        HttpsURLConnection
                .setDefaultHostnameVerifier(new HostnameVerifier() {

                    public boolean verify(String hostname,
                            SSLSession session) {
                        return true;
                    }
                });

        char[] passwKey = "password".toCharArray();
        KeyStore ts = KeyStore.getInstance("BKS");
                InputStream in = getResources().openRawResource(
            R.raw.YOUR_CERTIFICATE_FILE);
                InputStream is = getResources().openRawResource(
            R.raw.YOUR_CERTIFICATE_FILE);
        ts.load(in, passwKey);
        KeyManagerFactory tmf = KeyManagerFactory.getInstance("X509");
        tmf.init(ts, passwKey);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(tmf.getKeyManagers(),
                new X509TrustManager[] { new MyX509TrustManager(is,
                        "password".toCharArray()) }, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());

                URL url = new URL(Commons.ApiCall);

        HttpsURLConnection connection = (HttpsURLConnection) url
                .openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Username", Username);
        connection.setRequestProperty("Password", Password);

         BufferedReader bin = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));

         StringBuffer sb = new StringBuffer();

        while ((line = bin.readLine()) != null) {
            sb.append(line);
        }


        in.close();  
                is.close();  
    } catch (Exception e) { // should never happen
        e.printStackTrace();
        Log.d("Err", e.toString());
    }

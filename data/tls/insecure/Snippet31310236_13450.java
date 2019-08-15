public static String getConnResponse(String url, String input,
            boolean isGet, boolean isJson) throws IOException {

        if (Constants.SocketFactory == null) {
            CertificateFactory cf;
            try {
                cf = CertificateFactory.getInstance("X.509");
                InputStream caInput = new URL("URL_OF_CERTIFICATE").openStream();
                Certificate ca = cf.generateCertificate(caInput);

                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                // Create a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory
                        .getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory
                        .getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // Create an SSLContext that uses our TrustManager
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);
                Constants.SocketFactory = context.getSocketFactory();
            } catch (CertificateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (KeyStoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (KeyManagementException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        HttpURLConnection conn;
        if (isGet) {
            if (input == null) {
                conn = (HttpURLConnection) new URL(url).openConnection();
            } else {
                conn = (HttpURLConnection) new URL(url + "?" + input)
                .openConnection();
            }

            if (Constants.SocketFactory!=null){
                ((HttpsURLConnection) conn).setSSLSocketFactory(Constants.SocketFactory);
            }
            conn.setRequestProperty("Accept", "application/json,text/html");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Cookie", input);
        } else {
            conn = (HttpURLConnection) new URL(url).openConnection();
            if (Constants.SocketFactory!=null){
                ((HttpsURLConnection) conn).setSSLSocketFactory(Constants.SocketFactory);
            }
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", isJson ? "application/json"
                    : "application/x-www-form-urlencoded");

            OutputStream os = conn.getOutputStream();
            if(input!=null){
                os.write(input.getBytes("UTF-8"));
            }
            os.flush();
            os.close();
        }

        try {
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            is.close();
            conn.disconnect();
            return sb.toString();
        } catch (SocketException e) {// connection reset
            return null;
        } catch (Exception e) {// connection reset
            return null;
        }
    }

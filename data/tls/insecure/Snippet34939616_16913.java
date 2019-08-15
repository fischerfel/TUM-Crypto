   private String downloadUrl(String myurl) throws IOException {
        String cad="";
        InputStream is = null;

        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();


            SSLContext sc;
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            conn.setSSLSocketFactory(sc.getSocketFactory());

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.connect();

            conn.getOutputStream().write(("username=" + user).getBytes());
            conn.getOutputStream().write( ("password=" + password).getBytes());

            is = conn.getInputStream();


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String line;

            while (((line = in.readLine()) != null)){
                cad=cad+line+"\r\n";
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return cad;
    }

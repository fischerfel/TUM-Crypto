 you can upload large jsonstring using buffer please use bellow code .

HttpsURLConnection connection = null;
        OutputStream os = null;
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();

            SSLContext contextSSL = SSLContext.getInstance("TLS");
            contextSSL.init(null, new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(contextSSL.getSocketFactory());
           MySSLFactory(context.getSocketFactory()));
            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setChunkedStreamingMode(0);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", auth);
            connection.setConnectTimeout(timeoutMillis);
            OutputStream os ;
            if (input != null && !input.isEmpty()) {
                os = connection.getOutputStream();
               InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
                BufferedInputStream bis = new BufferedInputStream(stream, 8 * 1024);
                byte[] buffer = new byte[8192];
                int availableByte = 0;
               while ((availableByte = bis.read(buffer)) != -1) {
                   os.write(buffer, 0, availableByte);
                   os.flush();
               }

            }
            int responseCode = connection.getResponseCode();

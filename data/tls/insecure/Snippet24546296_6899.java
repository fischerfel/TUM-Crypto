String response = null;
         URL url;
         HttpsURLConnection con = null;

         try {

            // Create a context that doesn't check certificates.
                SSLContext ssl_ctx = SSLContext.getInstance("TLS");
                TrustManager[ ] trust_mgr = get_trust_mgr();
                ssl_ctx.init(null,                // key manager
                             trust_mgr,           // trust manager
                             new SecureRandom()); // random number generator
                HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

            url = new URL("https://example.org/v1/user/login/");

            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

            // Guard against "bad hostname" errors during handshake.
                con.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String host, SSLSession sess) {
                        if (host.equals("localhost")) return true;
                        else return false;
                    }
                });

            con.setRequestProperty("Content-Type", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("PUT");
            PrintStream ps = null;
            ps = new PrintStream(con.getOutputStream());
            ps.print(aLoginJS);
            ps.close();

            //dump all the content
            StringBuffer string = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            string = new StringBuffer();
            String inputLine = null;
            while ((inputLine = br.readLine()) != null) {
            string.append(inputLine);
            }

            response = string.toString();
            br.close();

         } catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         } catch (KeyManagementException e) {
             e.printStackTrace();
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
            if (con != null) {
                con.disconnect();
            }
         }

         return response;

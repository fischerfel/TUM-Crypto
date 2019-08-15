URL url = new URL("https://myloginform");
            trustAllHosts(); //because the certificate is not singed
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(DO_NOT_VERIFY);
            conn.setInstanceFollowRedirects(false);
            conn.setDoOutput(true);

            //Connect to login-page and send login data
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();     

            //get cookies THIS WORKS ONLY ON ANDROID 2.3 AND ABOVE
            List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
            conn.disconnect();

            //connect to overview page
            url = new URL("https://mynextpage");
            trustAllHosts();
            conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(DO_NOT_VERIFY);
            conn.setInstanceFollowRedirects(false);




            //Send cookies for identification - THIS WILL THROW A NULLPOINTER EXCEPTION
            for (String cookie : cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
            }





            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            while (!(line2.contains("</html>"))) {
                line = rd.readLine();
                line2 += line;
            }
            // wr.close();
            rd.close();

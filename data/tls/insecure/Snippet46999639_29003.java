SSLContext sc = SSLContext.getInstance("SSL");
            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                     new javax.net.ssl.HostnameVerifier(){

                          public boolean verify(String hostname,
                                  javax.net.ssl.SSLSession sslSession) {
                              return true;
                          }
                    });

            HttpURLConnection conn = (HttpURLConnection) tnprestapplicationURL.openConnection();
            String USER_AGENT = "Mozilla/5.0";

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

public String createConnection (String urlS, String methodInvoked,String patchBody, String postBody,String putBody){
        URL url ;
        BufferedReader br = null;
        String toBeReturned="";
        try {
            url = new URL(urlS);

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Create an SSLContext that uses our TrustManager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);


            HttpsURLConnection  connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(60000);
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setHostnameVerifier(hostnameVerifier);


            if (patchBody  != null ){
                Log.i(TAG, " createConnection with PATH with body" );
                connection.setRequestMethod("PATCH");
                connection.setRequestProperty("data",patchBody);
                connection.addRequestProperty("Content-Type", "application/json");
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(patchBody);
                dStream.flush();
                dStream.close();
            }
            if (methodInvoked.equalsIgnoreCase("PATCH") && patchBody == null ){
                Log.i(TAG, " createConnection with PATH without body" );
                connection.setRequestMethod("PATCH");
//              connection.addRequestProperty("Content-Type", "application/json");
//              connection.setDoOutput(true);
            }
            if (postBody != null){
                Log.i(TAG, " createConnection with POST with body" );
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(postBody);
                dStream.flush();
                dStream.close();
            }

            if (methodInvoked.equalsIgnoreCase("POST") && postBody == null ){
                Log.i(TAG, " createConnection with POST without body" );
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
                //connection.addRequestProperty("Content-Type", "application/json");
            }

            if (putBody != null){
                Log.i(TAG, " createConnection with PUT with body" );
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.addRequestProperty("Content-Type", "application/json");
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(putBody);
                dStream.flush();
                dStream.close();
            }



            responseCode = connection.getResponseCode();
            InputStream in= null;
            if(responseCode >= HttpsURLConnection.HTTP_BAD_REQUEST)
            {   

                in = connection.getErrorStream();
                br = new BufferedReader( new InputStreamReader(connection.getErrorStream()));
                StringBuilder sb = new StringBuilder();
                String line = null; 
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                 String toBeReturned_1 = sb.toString();
                 Log.i(TAG, " createConnetion error received " +  responseCode  + "  " + toBeReturned_1) ;

            }
            else{


                br = new BufferedReader( new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null; 
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                toBeReturned = sb.toString();


            }


        } catch (MalformedURLException e) {
            error = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            error = e.getMessage();
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            try {
                if (br!=null)
                    br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Log.i(TAG, " createConnetion  finally returned" +  toBeReturned );
        return toBeReturned; 
    }

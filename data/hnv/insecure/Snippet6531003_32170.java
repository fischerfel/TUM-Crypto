    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

            // Set verifier     
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

           // Example send http request
           final String url = "https://(serveraddress).../";
           HttpPost httpPost = new HttpPost(url+ zAPIName);


            byte[] byteArray = (zUserNameP.trim() + ":" + zPasswordP.trim()).getBytes();
            String EncodedString = Base64.encodeBytes(byteArray);
            httpPost.setHeader("Authorization", "Basic " + EncodedString);

            httpPost.setEntity(zStringEntityL);
            HttpResponse aHttpResponseL;
            aHttpResponseL = client.execute(httpPost);
            BufferedReader aBufferedReaderL = new BufferedReader(new InputStreamReader(
                    aHttpResponseL.getEntity().getContent()));
            StringBuffer aStringBufferL = new StringBuffer("");
            String zLineL = "";
            String zLineSeparatorL = System.getProperty("line.separator");
            while ((zLineL = aBufferedReaderL.readLine()) != null) {
                aStringBufferL.append(zLineL + zLineSeparatorL);
            }
            aBufferedReaderL.close();
            zResponserResultL = aStringBufferL.toString();
            aResultL.StatusCode = aHttpResponseL.getStatusLine().getStatusCode();
            ResposeMessage aMessageL = new ResposeMessage();
            Gson gson = new Gson();
            aMessageL = gson.fromJson(zResponserResultL, ResposeMessage.class);
            aResultL.ResponseString = aMessageL.message;    

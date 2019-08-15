    private String steamRequest( String get, String post ) {

    final int MAX_RETRIES = 3;
    int numberOfTries = 0;

    HttpsURLConnection request = null;

    while(numberOfTries < MAX_RETRIES) {

        if (numberOfTries != 0) {
            Log.d(TAG, "Retry -> " + numberOfTries);
        }

        try {
            request = (HttpsURLConnection) new URL("https://api.steampowered.com/" + get).openConnection(); //or 63.228.223.110/ ???

            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            String host = "api.steampowered.com";
            int port = 443;
            int header = 0;

            socketFactory.createSocket(new Socket(host, port), host, port, false);
            request.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            request.setSSLSocketFactory(socketFactory);
            request.setDoOutput(false);
            //            request.setRequestProperty("Host", "api.steampowered.com:443");
            //            request.setRequestProperty("Protocol-Version", "httpVersion");
            request.setRequestProperty("Accept", "*/*");
            request.setRequestProperty("Accept-Encoding", "gzip, deflate");
            request.setRequestProperty("Accept-Language", "en-us");
            request.setRequestProperty("User-Agent", "Steam 1291812 / iPhone");
            request.setRequestProperty("Connection", "close");

            if (post != null) {
                byte[] postBytes;

                try {
                    request.setRequestMethod("POST");

                    postBytes = post.getBytes("US-ASCII");

                    //                    request.setRequestProperty("Content-Length", Integer.toString(postBytes.length));
                    request.setFixedLengthStreamingMode(postBytes.length);
                    request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    PrintWriter out = new PrintWriter(request.getOutputStream());
                    out.print(post);
                    out.close();

                    //                    DataOutputStream requestStream = new DataOutputStream(request.getOutputStream());
                    ////                    OutputStreamWriter stream = new OutputStreamWriter(request.getOutputStream());
                    ////                    stream.write(postBytes, 0, postBytes.length);
                    //                    requestStream.write(postBytes, 0, postBytes.length);
                    //                    requestStream.close();

                    message++;

                } catch (ProtocolException e) {
                    e.printStackTrace();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                int statusCode = request.getResponseCode();
                InputStream is;
                Log.d(TAG, "The response code of the status code is" + statusCode);
                if (statusCode != 200) {
                    is = request.getErrorStream();
                    Log.d(TAG, String.valueOf(is));
                }

                //            String src = null;
                //            OutputStreamWriter out = new OutputStreamWriter(request.getOutputStream());
                //            out.write(src);
                //            out.close();

                Scanner inStream = new Scanner(request.getInputStream());
                String response = "";
                while (inStream.hasNextLine()) {
                    response += (inStream.nextLine());
                }

                //                String src;
                //                BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
                //                StringBuilder builder = new StringBuilder();
                //                while ((src = in.readLine()) != null) {
                //                    builder.append(src);
                //                }

                //                String jsonData = builder.toString();
                Log.d(TAG, response); //jsonData

                //                in.close();

                return response; //jsonData

            } catch (MalformedURLException ex) {
                Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (request != null) {
                request.disconnect();
            }
        }

        numberOfTries++;
    }
    Log.d(TAG, "Max retries reached. Giving up on connecting to Steam Web API...");
    return null;
}

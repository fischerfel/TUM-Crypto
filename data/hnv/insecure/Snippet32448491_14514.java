@Override
    protected String doInBackground(Void... arg0) {
        try {

            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            //ALLOW_ALL_HOSTNAME_VERIFIER
            //BROWSER_COMPATIBLE_HOSTNAME_VERIFIER
            //STRICT_HOSTNAME_VERIFIER
            //===============================================================================
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
            //===============================================================================

            //HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (filePath != null) {
                if (isImage) {
                    for(int k=0;k<=50;k++){
                        Bitmap bm = BitmapFactory.decodeFile(filePath);
                        if (bm!=null){
                            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                            break;
                        }
                    }

                } else {
                    try {
                        File file = new File(filePath);
                        InputStream inputStream = new FileInputStream(file);
                        byte[] b = new byte[1024 * 8];
                        int bytesRead = 0;
                        while ((bytesRead = inputStream.read(b)) != -1) {
                            bos.write(b, 0, bytesRead);
                        }
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            byte[] dataFile = bos.toByteArray();
            ByteArrayBody bab = new ByteArrayBody(dataFile, fileName);

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart(fileName, bab);
            for (int i = 0; i < params.size(); i++) {
                if(params.get(i).getValue()!=null){
                    reqEntity.addPart(params.get(i).getName(), new StringBody(params.get(i).getValue()));
                }
            }
            InputStream data = null;
            HttpResponse response;
            request.setEntity(reqEntity);
            response = httpClient.execute(request);

            data = response.getEntity().getContent();
            String s = ConvertInputStreamToString(data);
            return s;
        } catch (SocketTimeoutException e) {
            Toast.makeText(UILApplication.getAppContext(), "Socket Time Out", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            Toast.makeText(UILApplication.getAppContext(), "Connection Time Out", Toast.LENGTH_LONG).show();
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

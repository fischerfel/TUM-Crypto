HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                        SchemeRegistry registry = new SchemeRegistry();
                        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                        registry.register(new Scheme("https", socketFactory, 8443));

                        DefaultHttpClient client = new DefaultHttpClient();

                        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
                        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());


                        HttpParams httpParameters = new BasicHttpParams();
                        HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.HTTP_CONNECTION_TIMEOUT);

                        HttpConnectionParams.setSoTimeout(httpParameters, Constants.HTTP_CONNECTION_TIMEOUT);

                        HttpGet httpget = new HttpGet(Utility.getCloudUrl());

                        try {
                            HttpResponse response = httpClient.execute(httpget);

                            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                            StringBuffer sb = new StringBuffer("");
                            String l = new String();
                            while ((l = in.readLine()) !=null){
                                sb.append(l);
                            }
                            in.close();
                            String data = sb.toString();
                            if (data.length() > 0){

                                synchronized (lock) {
                                    isInternetAvialbale = true;
                                }
                                listener.onInternetReachable();

                            }else{


                                synchronized (lock) {
                                    isInternetAvialbale = false;
                                }
                                listener.onInternetNotReachable();
                            }
                        } catch (IOException e) {


                            synchronized (lock) {
                                isInternetAvialbale = false;
                            }
                            listener.onInternetNotReachable();
                            return;
                        }

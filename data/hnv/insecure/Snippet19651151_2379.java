 int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT>8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            DefaultHttpClient client = new DefaultHttpClient();
            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr,client.getParams());
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
             // DefaultHttpClient client = new DefaultHttpClient();

                // ssl code



                    try 
                    {
                        String s= "http://maps.googleapis.com/maps/api/directions/json?origin="+etBookPick.getText()+"&destination="+etBookDrop.getText()+"&sensor=false";

                        HttpGet get = new HttpGet(s.trim());

                        try 
                        {
                            InputStream is = null;
                            JSONObject jObj = null;
                            String jsonn = "";
                            HttpResponse hresponse = httpClient.execute(get);
                            HttpEntity httpEntity = hresponse.getEntity();
                            is = httpEntity.getContent();       


                            try {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                                StringBuilder sb = new StringBuilder();
                                String line = null;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line + "\n");
                                }
                                is.close();
                                jsonn = sb.toString();
                            } catch (Exception e) {
                                Log.e("Buffer Error", "Error converting result " + e.toString());
                            }

                            // try parse the string to a JSON object
                            try {
                                jObj = new JSONObject(jsonn);
                            } catch (JSONException e) {
                                Log.e("JSON Parser", "Error parsing data " + e.toString());
                            }


                            JSONArray routeArray = jObj.getJSONArray("routes");
                            JSONObject routes = routeArray.getJSONObject(0);

                            JSONArray newTempARr = routes.getJSONArray("legs");
                            JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

                            JSONObject distOb = newDisTimeOb.getJSONObject("distance");
                            JSONObject timeOb = newDisTimeOb.getJSONObject("duration");


                            Toast.makeText(BookCab.this, distOb.getString("text"), Toast.LENGTH_LONG).show();
                             distanceinKm=  distOb.getString("text");
                            distanceinKm = distanceinKm.replace("km", "").trim();




                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

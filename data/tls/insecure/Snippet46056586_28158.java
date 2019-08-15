 public void makeRequest(){


 try {

                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    InputStream caInput;


                    //I have stored my C.A certificates in Resources.raw file, Get it this way
                    caInput = new BufferedInputStream(getResources().openRawResource(R.raw.godaddysecure));



                    Certificate ca;

                    try {
                        ca = cf.generateCertificate(caInput);
                        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                        Log.d("MyApp", "Certificate " + ((X509Certificate) ca).getSubjectDN());
                    } finally {
                        caInput.close();
                    }


                    // Create a KeyStore containing our trusted CAs


                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null, null);
                    keyStore.setCertificateEntry("ca", ca);


                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                    tmf.init(keyStore);

                    Log.d("MyApp", "KeyStore Initialized ");


                    // Create a TrustManager that trusts the CAs in our KeyStore


                    // Create an SSLContext that uses our TrustManager


                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, tmf.getTrustManagers(), null);
                    Log.d("MyApp", "SSL context call ");
                    URL url = new URL("Your String URl here");


                    urlConnection =
                            (HttpsURLConnection) url.openConnection();


                    // Tell the URLConnection to use a SocketFactory from our SSLContext

                    //Replace below with your own request properties
                    urlConnection.setRequestProperty("Your Request properties here");
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Accept", "text/xml");
                    urlConnection.setSSLSocketFactory(context.getSocketFactory());


                    try {

                        //Write your output stream to server here
                        String body = "Boody of request i was sending";

                        OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                        wr.write(body);

                        wr.flush();

                    } catch (Exception e) {

                        Log.d("MyApp", "Body write Exception " + e.toString());
                    }


                    int responseCode = urlConnection.getResponseCode();


                    InputStream in = urlConnection.getInputStream();


                    //get your server response as below
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line = "";
                    StringBuilder bd = new StringBuilder();

                    System.out.println("Before String builder " + reader);

                    while ((line = reader.readLine()) != null) {

                        System.out.println("Before String builder while loop");
                        String output = bd.append(line).toString();
                        System.out.println("Output " + output + " code " + responseCode);
                        strResponse = output;


                    }

                } catch (RuntimeException e) {

                    Log.d("Mfinance", "Login Activity Exception");
                  //  e.printStackTrace();
                } finally {
                    try {

                        reader.close();
                    } catch (Exception e) {

                        Log.d("Mfinance", "Loans Activity Exception");
                      //  e.printStackTrace();
                    }
                }}

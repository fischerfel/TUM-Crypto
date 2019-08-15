try {
                File dir = new File(getExternalFilesDir(null).getAbsolutePath() + "/file");
                if (dir.exists() == false) {
                    dir.mkdirs();
                }
                URL url = new URL(urlLink );

                File outputFile = new File(dir, fileName);
                /* Open a connection to that URL. */
                try
                {
                    Log.d(TAG, "Initiating File Download");
                    SSLContext sslcontext = SSLContext.getInstance("TLSv1");

                    sslcontext.init(null,
                            null,
                            null);
                    SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

                    HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);

                    HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.setDoOutput(true);
                    c.setConnectTimeout(15000);
                    c.connect();


                    FileOutputStream fos = new FileOutputStream(outputFile);
                    InputStream is = c.getInputStream();

                    byte[] buffer = new byte[4096];
                    int len1 = 0;

                    while ((len1 = is.read(buffer)) != -1)
                    {
                        fos.write(buffer, 0, len1);
                    }

                    fos.close();
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

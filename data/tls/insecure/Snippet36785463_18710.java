                    public boolean verify(String hostname,
                                          javax.net.ssl.SSLSession sslSession) {
                        if (hostname.equals("<hostname>")) {
                            return true;
                        }
                        return false;
                    }
                });


                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
              try
              {
              KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509","SunJSSE");
              KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE);
              File cert = new File(KEY_STORE);
              InputStream stream = new FileInputStream(cert);
              ks.load(stream, KEY_STORE_PASS.toCharArray());
              stream.close();
              kmf.init(ks,KEY_STORE_PASS.toCharArray());

              SSLContext context = SSLContext.getInstance("TLS");
              context.init(kmf.getKeyManagers(),null, new SecureRandom());            
              SSLSocketFactory factory = context.getSocketFactory();               
              conn.setSSLSocketFactory(factory);

              }
              catch(Exception e)
              {
                  System.out.println(e);
              }

            JSONObject obj = new JSONObject();
             obj.put("id","abc");
             obj.put("name","surya");
             obj.put( "domain","IT");     
             String input=obj.toString();

         //send request to server....            

             OutputStream os = conn.getOutputStream();
             os.write(input.getBytes());
             os.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
             System.out.println("Response code: "+conn.getResponseCode());
              System.out.println("Response message: "+conn.getResponseMessage());
            conn.disconnect();

          } catch (MalformedURLException e) {

            e.printStackTrace();

          } catch (IOException e) {

            e.printStackTrace();

         }

        }

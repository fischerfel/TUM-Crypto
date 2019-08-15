HttpsURLConnection con = null;
    try
    {
        System.out.println("Calling webservice ..... ");

        char[] passw = "mypassword".toCharArray();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        InputStream keyInput = new FileInputStream( "/usr/local/KEYSTORE.p12");

        ks.load(keyInput, passw );
        keyInput.close();

        System.out.println("Reading certificate file Completed");

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, passw);

        System.out.println("Set SSL certificate");
        SSLContext sclx = SSLContext.getInstance("SSL");
        sclx.init( kmf.getKeyManagers(), null, new SecureRandom());
        SSLContext.setDefault(sclx);

        HttpsURLConnection.setDefaultSSLSocketFactory(sclx.getSocketFactory());
        URL url = new URL("https://myurl.server.com");
        con = (HttpsURLConnection)url.openConnection();
        System.out.println("Connection successfull");
        //con.setSSLSocketFactory(sclx.getSocketFactory());

        System.out.println("Token Id 026F800E-562D-4B0F-BC56-AC16895072F4");

        if(con!=null)
        {
            System.out.println("Inside connection"); 
            con.setRequestProperty("Content-Type","application/json;charset=utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Token-Id","026F800E-562D-4B0F-BC56-AC16895072F4");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);
            con.setRequestMethod("POST");
            con.connect();
            OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream(), "UTF-8");

            os.write("request String");
            os.flush();
            os.close();

            InputStreamReader dis = new InputStreamReader(con.getInputStream(),"UTF-8");

            if (null != dis)
            {
                System.out.println("Reading response from webservice 1");

                BufferedReader reader = new BufferedReader(dis);
                String line;
                while((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }
    }
    catch(IOException ioex)
    {
        System.err.println("Exception   : "+ioex);
        e.printStackTrace();            
    }
    catch(Exception ex){
        ex.printStackTrace();           
    }
    finally{
        if(con != null) {
            con.disconnect();
        }
    }  

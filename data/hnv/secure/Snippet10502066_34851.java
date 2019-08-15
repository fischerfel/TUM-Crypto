    try
    {
        url = new URL(url1);



        trustAllHosts();
           HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
       https.setHostnameVerifier(DO_NOT_VERIFY);
        http = https;

        InputStream in = new BufferedInputStream(http.getInputStream());

        sAuthenticateP1 = iStream_to_String(in);
        in.close();


    } catch (Exception e)
    {
        e.printStackTrace();
    }

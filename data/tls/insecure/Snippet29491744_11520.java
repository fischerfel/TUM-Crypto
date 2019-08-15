SSLContext ctx = SSLContext.getInstance("TLS");
        try
        {
        ctx.init(null, new TrustManager[] { new UDMX509TrustManager(
            Constants.UDM_SERVER_SOURCE) }, new SecureRandom());
        } catch (Exception e)
        {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }    
URL url = new URL(urlString);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url
            .openConnection();
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /*
                           * milliseconds
                           */);
        urlConnection.setSSLSocketFactory(ctx.getSocketFactory());
        if (jsonObject != null)
        {
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type",
            "application/json");
        DataOutputStream printout = new DataOutputStream(
            urlConnection.getOutputStream());
        printout.writeBytes(URLEncoder.encode(jsonObject.toString(),
            "UTF-8"));
        printout.flush();
        printout.close();
        InputStream in = urlConnection.getInputStream();
        Reader reader = null;
        reader = new InputStreamReader(in, "UTF-8");
        char[] buffer = new char[1000];
        reader.read(buffer);
        response = new String(buffer);
        MLog.v("response is", response);
        }/*
          * catch (Exception e) { e.printStackTrace(); } }
          */else
        {
        return response = Constants.NETWORK_UNAVAILABLE;
        }
        /*
         * } catch (Exception e) { e.printStackTrace(); }
         */

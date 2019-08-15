        URL t_url = new URL(urlStr);
        HttpsURLConnection con = (HttpsURLConnection) t_url.openConnection();

        //Create SSL con
        SSLContext sc;
        sc = SSLContext.getInstance("TLS");
        sc.init(null, null, new java.security.SecureRandom());
        con.setSSLSocketFactory(sc.getSocketFactory());

        con.setReadTimeout(10000);
        con.setConnectTimeout(10000);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        con.setRequestProperty("phone_number", mPhone_number);
        con.setUseCaches(false);

        con.connect();

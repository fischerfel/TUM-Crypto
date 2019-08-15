        URI uri = new URI(URIString);

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);

        Scheme sch = new Scheme("https", socketFactory, 443);
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);

        HttpGet httpget = new HttpGet(uri);

        System.out.println(httpget.getURI());

        HttpResponse response = httpclient.execute(httpget, myLibrary.localContext);

        HttpEntity entity = response.getEntity();

        InputStream is = entity.getContent();

        Drawable myImage= Drawable.createFromStream(is, "Image");
        entity.consumeContent();

        if (myImage!=null){
            System.out.println("PhotoID " + PhotoID + " is NOT null!");
            return myImage;
        } else {
            System.out.println("PhotoID " + PhotoID + " is null!");
            return null;
        }

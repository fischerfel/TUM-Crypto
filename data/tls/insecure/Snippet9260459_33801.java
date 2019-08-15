    public boolean shouldOverrideUrlLoading(WebView view, String url) 
    {       try
        {
            InputStream keyStoreStream = getResources().openRawResource(R.raw.jssecacerts);
            KeyStore keyStore = null;
            keyStore = KeyStore.getInstance("BKS");
            keyStore.load(keyStoreStream, "mypwd".toCharArray());

                KeyManagerFactory keyManagerFactory = null;
            keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "mypwd".toCharArray());

           SSLContext context = SSLContext.getInstance("TLS");
           context.init(keyManagerFactory.getKeyManagers(), null, null);

           alertbox("before response", url);
           URL url1 = new URL(url);
           HttpsURLConnection urlConnection = (HttpsURLConnection) url1.openConnection();
           urlConnection.setSSLSocketFactory(context.getSocketFactory());
           InputStream in = urlConnection.getInputStream();
           alertbox("response", convertToString(in));
           view.loadData(convertToString(in), "text/html", "utf-8");               
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

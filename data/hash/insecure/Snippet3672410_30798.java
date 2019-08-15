            boolean validated = false;
            byte[] Resp = AndAppStorePurchaseChecking("test@andappstore.com","98765", "543788");
            if(isValidPurchase(Resp))
                validated = true;


       private byte[] AndAppStorePurchaseChecking(String u, String d, String a) {

            final HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            final SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http",PlainSocketFactory.getSocketFactory(), 80));
            final ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
            HttpClient httpClient = new DefaultHttpClient(manager, params);

           byte[] buffer = null; 
           final Uri.Builder uri = new Uri.Builder();
            uri.path("/AndroidApplications/purchaseCheck");

            uri.appendQueryParameter("u", u);
            uri.appendQueryParameter("d", d);
            uri.appendQueryParameter("a", a);                               

            HttpEntity entity = null;
            HttpHost host = new HttpHost("andappstore.com", 80, "http");
            try {
                final HttpResponse response = httpClient.execute(host, new HttpPost(uri.build().toString()));
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    entity = response.getEntity();
                    final InputStream in = entity.getContent();
                    //FileOutputStream fos = openFileOutput("AndLicense.001", MODE_PRIVATE);
                    try {
                        buffer = new byte[20];
                        in.read(buffer);
//                      int len;
//                      while((len = in.read(buffer)) > 0 ) {
//                          fos.write(buffer, 0, len);
//                      }
                    } finally {
                        //fos.close();
                    }
                }               
            } catch (Exception ex) {
                new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Error validating installation")
                    .setMessage(ex.getMessage())
                    .setPositiveButton("OK", null)
                    .show(); 
            } finally {
                if (entity != null) {
                    try {
                        entity.consumeContent(); 
                    } catch(IOException ioe) {
                        // Ignore errors during consumption, there is 
                        // no possible corrective action.
                    }
                }
            }  
            return buffer;
        }   


    public boolean isValidPurchase(final byte[] fromServer) {           
          if( fromServer == null || fromServer.length == 0 ) 
            return false;

          try{
              MessageDigest md = MessageDigest.getInstance("SHA1");
              byte[] digest = md.digest("98765PURCHASING-API-KEY".getBytes("UTF-8"));
              return Arrays.equals(fromServer, digest);
          } catch(Exception ex) {
              return false;
          }
        }   

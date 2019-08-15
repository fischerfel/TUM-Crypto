 try
 {
   final KeyStore ks = KeyStore.getInstance("BKS");
   final InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.certs);
   ks.load(inputStream, getApplicationContext().getString(R.string.store_pass).toCharArray());
   inputStream.close();

   TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
   tmf.init(ks);

   SSLContext context = SSLContext.getInstance("TLS");
   context.init(null, tmf.getTrustManagers(), new SecureRandom());

   URL url = new URL("https://www.mywebsite.com/");
   HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
   urlConnection.setSSLSocketFactory(context.getSocketFactory());
   InputStream in = urlConnection.getInputStream();
 }
 catch(Exception e) {
   Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
   // the toast appear empty (described in question)
 }

  KeyStore trusted = KeyStore.getInstance("BKS");
  InputStream in = c.getResources().openRawResource(R.raw.test);

  trusted.load(in, "password".toCharArray());


 TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
 trustManagerFactory.init(trusted);

 KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
 kmf.init(trusted, "password".toCharArray());

 SSLContext context = SSLContext.getInstance("TLS");
 context.init(kmf.getKeyManagers(), tm, null);



  URL request = new URL(URL);

  urlConnection = (HttpsURLConnection) request.openConnection();


  urlConnection.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

  urlConnection.setSSLSocketFactory(context.getSocketFactory());

  urlConnection.setDoInput(true);

  urlConnection.setReadTimeout(200000);


  urlConnection.connect();

  InputStream ina = urlConnection.getInputStream();

  BasicHttpEntity res = new BasicHttpEntity();

  res.setContent(ina);

  HttpResponse resp = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                urlConnection.getResponseCode(), "");
  resp.setEntity(res);

  HttpEntity responseEntity = resp.getEntity();

  InputStream data = responseEntity.getContent();

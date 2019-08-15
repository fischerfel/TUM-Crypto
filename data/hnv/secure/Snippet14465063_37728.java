KeyStore trustStore = KeyStore.getInstance("BKS");
InputStream is = this.getAssets().open("discretio.bks");
trustStore.load(is, "discretio".toCharArray());
is.close();

TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
tmf.init(trustStore);
SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), null);

URL request = new URL(url);
HttpsURLConnection urlConnection = (HttpsURLConnection) request.openConnection();

//ensure that we are using a StrictHostnameVerifier
urlConnection.setHostnameVerifier(new StrictHostnameVerifier());
urlConnection.setSSLSocketFactory(context.getSocketFactory());
urlConnection.setConnectTimeout(15000);

InputStream in = urlConnection.getInputStream();
//I don't want to change my function's return type (laziness) so I'm building an HttpResponse
BasicHttpEntity res = new BasicHttpEntity();
res.setContent(in);
HttpResponse resp = new BasicHttpResponse(HttpVersion.HTTP_1_1, urlConnection.getResponseCode(), "");
resp.setEntity(res);

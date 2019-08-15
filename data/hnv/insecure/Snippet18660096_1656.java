   U can call like this 


                  try {
        SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
        sslFactory
                .setHostnameVerifier(SSLSocketFactory.
                    ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        registry.register(new Scheme("https", sslFactory, 443));
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                params, registry);
        DefaultHttpClient httpClient = new DefaultHttpClient(ccm, params);
        HttpPost httpPost = new HttpPost(url);
        HttpHost proxy = new HttpHost("50.104.5.277", 23333);

        //httpPost.setHeader("Content-Type", "text/xml");
        httpPost.setEntity(entity);
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                proxy);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        output = EntityUtils.toString(httpEntity);

        // Toast.makeText(getApplicationContext(), xml, 1000).show();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (UnrecoverableKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (KeyStoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return output;
}

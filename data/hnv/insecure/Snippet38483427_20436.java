try{
            int TIMEOUT_MILLISEC = 30000; // = 30 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { new CustomX509TrustManager() }, new SecureRandom());

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            DefaultHttpClient sslClient = new DefaultHttpClient(ccm, httpClient.getParams());

            HttpPost httpPost = new HttpPost(Url);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("data",new StringBody(obj.toString(), Charset.forName("UTF-8")));
            httpPost.setEntity(reqEntity);

            HttpResponse response = sslClient.execute(httpPost);
            StatusCode = response.getStatusLine().getStatusCode();
            Log.e("Response", "statuscode " + StatusCode);

            HttpEntity entity = response.getEntity();
            Log.e("Response String ",""+entity.toString());
            if (entity != null) {
                responseString = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            System.out.println(e);
        }

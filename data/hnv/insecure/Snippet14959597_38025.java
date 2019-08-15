        KeyStore trustStore;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new EasySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            BasicHttpParams params = new BasicHttpParams();
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(ccm, params);
            HttpsURLConnection.setDefaultHostnameVerifier(sf.getHostnameVerifier());
            HttpGet getRequest = new HttpGet(url);
            BasicHttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 15000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            httpClient.setParams(httpParameters);
            HttpResponse getResponse = httpClient.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) 
            { 
                return null;
            }
            HttpEntity getResponseEntity = getResponse.getEntity();
            String content = EntityUtils.toString(getResponseEntity);
            InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
            return is;
        } catch (KeyStoreException e1) {
            e1.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    return null;

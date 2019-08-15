public String httpRestGetCallwithCertificate(String url, String payLoad) {
        String result = null;
        DefaultHttpClient httpClient = null;
        KeyStore trustStore = null;
        try {
            httpClient = new DefaultHttpClient(getHttpParams());
            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            HttpHost targetHost = new HttpHost("hostname","portno","https");
            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                    new UsernamePasswordCredentials("username","password"));
            trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());

            InputStream instream = mContext.getResources().openRawResource(R.raw.truststore);
            try {
                trustStore.load(instream, "password".toCharArray());

            } finally {
                try { instream.close(); } catch (Exception ignore) {}
            }
            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            Scheme sch = new Scheme("https", socketFactory, 443);
            registry.register(sch);
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            httpClient.getConnectionManager().getSchemeRegistry().register(sch);
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpget);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return "success"
            }
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
        } catch (ConnectTimeoutException exception) {
            Log.e(TAG, "Network connetcion is not available");
            exception.printStackTrace();
        }catch(SocketTimeoutException exception){
            Log.e(TAG, "Socket timed out.");
            exception.printStackTrace();
        } catch (IOException exception) {
            Log.v("IO Exception", exception.toString());
            exception.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            if (httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            }
            httpPost = null;
        }
        return "fail";
    }

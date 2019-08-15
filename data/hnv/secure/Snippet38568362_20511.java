public static CookieManager msCookieManager = null;
private boolean isPinBuild=false;

public static String[] post(Context context, String Url, List<NameValuePair> values, List<NameValuePair> header_list)
        throws ConnectTimeoutException {

    String[] result = { "", "" };
    try {
        URL url = new URL(Url);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        Log.i("TAG_URL : ", Url);

        if (isPinBuild) {
            KeyPinStore keystore = KeyPinStore.getInstance(context);
            javax.net.ssl.SSLSocketFactory factory = keystore.getContext().getSocketFactory();
            SSLSocket socket = (SSLSocket)factory.createSocket();
            socket.setEnabledCipherSuites(new String[]{"RC4-MD5", "DES-CBC-SHA", "DES-CBC3-SHA"});
            socket.setEnabledProtocols(new String[] {"TLSv1.1", "TLSv1.2"});
            socket.setSoTimeout(60000);
            urlConnection.setSSLSocketFactory(factory);
        } else {
            KeyUnPinStore keystore = KeyUnPinStore.getInstance(context);
            javax.net.ssl.SSLSocketFactory factory = keystore.getContext().getSocketFactory();
            SSLSocket socket = (SSLSocket)factory.createSocket();
            socket.setEnabledCipherSuites(new String[]{"RC4-MD5", "DES-CBC-SHA", "DES-CBC3-SHA"});
            socket.setEnabledProtocols(new String[] {"TLSv1.1", "TLSv1.2"});
            socket.setSoTimeout(60000);
            urlConnection.setSSLSocketFactory(factory);
        }
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
        urlConnection.setRequestMethod("POST");
        urlConnection.setConnectTimeout(connection_timeout);
        urlConnection.setHostnameVerifier(hostnameVerifier);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        for (NameValuePair header : header_list) {
            urlConnection.addRequestProperty(header.getName(), header.getValue());
        }

        if (msCookieManager == null) {
            msCookieManager = new CookieManager();
        }

        if (msCookieManager != null && msCookieManager.getCookieStore().getCookies().size() > 0) {
            urlConnection.setRequestProperty(COOKIE, TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));
        }
        Log.i("TAG_POST_COOKIE : ", msCookieManager.getCookieStore().getCookies().toString());

        String postData = "";
        for (NameValuePair value : values) {
            postData = postData + value.getName() + "=" + URLEncoder.encode(value.getValue(), "UTF-8") + "&";
        }
        if (!TextUtils.isEmpty(postData) && postData.length() > 2) {
            postData = postData.substring(0, postData.length() - 1);
        }

        Log.i("TAG_POSTDATA : ", postData);

        urlConnection.setUseCaches(false);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

        urlConnection.setFixedLengthStreamingMode(postData.getBytes().length);
        PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
        out.print(postData);
        out.close();

        // always check HTTP response code first
        int responseCode = urlConnection.getResponseCode();
        result[0] = responseCode + "";

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Get Response
            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            /*if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    if (HttpCookie.parse(cookie).get(0).toString().contains(JSESSIONID)) {
                        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }
            }*/

            if (!TextUtils.isEmpty(response)) {
                result[0] = HttpConnectionUrl.RESPONSECODE_REQUESTSUCCESS;
                result[1] = response.toString();
                Log.i("TAG_RESPONSE : ", result[1]);
            }

        }

    } catch (UnsupportedEncodingException e) {
        result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
        e.printStackTrace();
    } catch (ClientProtocolException e) {
        result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
        e.printStackTrace();
    } catch (ConnectTimeoutException e) {
        result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
        e.printStackTrace();
    } catch (IOException e) {
        result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
        e.printStackTrace();
    } catch (Exception e) {
        result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
        e.printStackTrace();
    }
    return result;
}

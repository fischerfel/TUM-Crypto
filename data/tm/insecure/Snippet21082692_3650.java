    public class PageGetter {
    private static final Log log = LogFactory.getLog("serviceLogger");
    private static SyncBasicHttpParams httpParams = null;
    private HttpClient httpClient = null;
    private static PoolingClientConnectionManager connectionManager = null;
    private static Integer errorSleepTime = new Integer(PropertyGetter.getInstance().getProperty("errorpagesleeptime"));

    public String getFinalRedirectURL(String url) throws ClientProtocolException, IOException {
        if (url == null) {
            return null;
        }
        if (!url.trim().startsWith("http://")) {
            url = "http://" + url;
        }
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpget = new HttpGet(url);
        if (connectionManager == null || httpParams == null || this.httpClient == null) {
            this.initHttpManager();
        }
        this.httpClient.execute(httpget, localContext);
        HttpHost target = (HttpHost) localContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        String finalURL = target.toString();
        httpget.abort();
        return finalURL;
    }

    private void initHttpManager() {
        // Create and initialize HTTP parameters
        if (httpParams == null) {
            httpParams = new SyncBasicHttpParams();
            httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
            httpParams.setParameter(HTTP.CONTENT_ENCODING, "GBK");
            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        }
        if (connectionManager == null) {
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
            try {
                //Security Socket
                SSLContext ctx = SSLContext.getInstance("TLS");
                X509TrustManager tm = new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    }
                };
                ctx.init(null, new TrustManager[] { tm }, null);
                SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                schemeRegistry.register(new Scheme("https", 443, ssf));
            } catch (Exception e) {
                log.error("Fail to create connection manager " + e);
            }
            connectionManager = new PoolingClientConnectionManager(schemeRegistry);
        }
        this.httpClient = new DefaultHttpClient(connectionManager, httpParams);
        this.httpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        HttpClientParams.setCookiePolicy(this.httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
    }

    public String getPostPageContent(String url, List<NameValuePair> headers, List<NameValuePair> params) {
        String strPage = null;
        if (url == null) {
            return null;
        }
        if (!url.trim().startsWith("http")) {
            url = "http://" + url;
        }
        HttpPost httpPost = new HttpPost(url);
        HttpEntity entity = null;
        try {
            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            if (headers != null) {
                Iterator<NameValuePair> itrHeaders = headers.iterator();
                while (itrHeaders.hasNext()) {
                    NameValuePair header = itrHeaders.next();
                    httpPost.setHeader(header.getName(), header.getValue());
                }
            }
            HttpContext localContext = new BasicHttpContext();
            if (connectionManager == null || httpParams == null || this.httpClient == null) {
                this.initHttpManager();
            }
            HttpResponse response = this.httpClient.execute(httpPost, localContext);
            // get the response body as an array of bytes
            entity = response.getEntity();
            if (entity != null) {
                strPage = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            connectionManager = null;
            httpParams = null;
            this.httpClient = null;
            this.errorHandler(url, e);
        } finally {
            httpPost.abort();

            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    log.error("Error when consume entity ", e2);
                }
            }
        }
        if (strPage != null) {
            Pattern pattern = Pattern.compile("\\s");
            Matcher matcher = pattern.matcher(strPage);
            strPage = matcher.replaceAll(" ");
            strPage = strPage.replaceAll("////", "");
        }
        return strPage;
    }

    private String getURL(String url, List<NameValuePair> headers) {
        if (headers == null) {
            return url;
        }
        Map<String, String> map = new HashMap<String, String>();
        if (url.contains("?")) {
            String str = url.substring(url.indexOf("?") + 1);
            url = url.substring(0, url.indexOf("?"));
            String[] strs = str.split("&");
            for (int i = 0; strs != null && i < strs.length; i++) {
                if (strs[i].contains("=")) {
                    String key = strs[i].substring(0, strs[i].indexOf("="));
                    String value = strs[i].substring(strs[i].indexOf("=") + 1);
                    map.put(key, value);
                }
            }
        }
        Iterator<NameValuePair> itrHeaders = headers.iterator();
        while (itrHeaders.hasNext()) {
            NameValuePair header = itrHeaders.next();
            if (header != null && header.getName() != null && header.getValue() != null) {
                try {
                    map.put(URLEncoder.encode(header.getName(), "UTF-8"), URLEncoder.encode(header.getValue(), "UTF-8"));
                } catch (Exception e) {
                    log.error("Can not parse parameter " + e);
                }
            }
        }
        if (map.keySet() == null) {
            return url;
        }
        Iterator<String> itr = map.keySet().iterator();
        String parameters = "";
        while (itr.hasNext()) {
            String key = itr.next();
            if (key != null && key.length() > 0) {
                parameters = parameters + key + "=" + map.get(key) + "&";
            }
        }
        if (parameters.length() > 0) {
            url = url + "?" + parameters;
        }
        return url;
    }

    private void errorHandler(String url, Exception e) {
        try {
            Thread.sleep(errorSleepTime);
            log.error("无法打开链接" + url + " sleep " + errorSleepTime + e);
        } catch (Exception e1) {
            log.error("Error sleeping " + errorSleepTime + e1);
        }
    }

    /**
     * Get page as string from URL
     * 
     * @param url
     * @return
     */
    public String getPageContent(String url, List<NameValuePair> headers) {
        url = StringFilter.replaceUnicode(url);
        String strPage = null;
        if (url == null) {
            return null;
        }
        if (!url.trim().startsWith("http")) {
            url = "http://" + url;
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.lastIndexOf("/"));
        }
        HttpGet httpGet = null;
        HttpEntity entity = null;
        try {
            if (connectionManager == null || httpParams == null || this.httpClient == null) {
                this.initHttpManager();
            }
            url = this.getURL(url, headers);
            httpGet = new HttpGet(url);
            HttpContext localContext = new BasicHttpContext();
            HttpResponse response = this.httpClient.execute(httpGet, localContext);
            entity = response.getEntity();
            if (entity != null) {
                String charSet = ContentType.getOrDefault(entity).getCharset().toString();
                if (charSet == null) {
                    charSet = "UTF-8";
                }
                strPage = EntityUtils.toString(entity, charSet);
                strPage = strPage.replaceAll("\r", "");
                strPage = strPage.replaceAll("\n", "");
                //空格
                strPage = strPage.replaceAll("&nbsp;", "");
            }
        } catch (Exception e) {
            connectionManager = null;
            httpParams = null;
            this.httpClient = null;
            this.errorHandler(url, e);
        } finally {
            try {
                httpGet.abort();
            } catch (Exception e) {
                log.error("Error when httpclient aborting " + e);
            }

            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    log.error("Error when consume entity ", e2);
                }
            }
        }
        return strPage;
    }

    public String getPageContentSms(String url, List<NameValuePair> headers) {
        url = StringFilter.replaceUnicode(url);
        String strPage = null;
        if (url == null) {
            return null;
        }
        if (!url.trim().startsWith("http")) {
            url = "http://" + url;
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.lastIndexOf("/"));
        }
        HttpGet httpGet = null;
        HttpEntity entity = null;
        try {
            if (connectionManager == null || httpParams == null || this.httpClient == null) {
                this.initHttpManager();
            }
            httpGet = new HttpGet(url);
            HttpContext localContext = new BasicHttpContext();
            HttpResponse response = this.httpClient.execute(httpGet, localContext);
            entity = response.getEntity();
            if (entity != null) {
                String charSet = ContentType.getOrDefault(entity).getCharset().toString();
                if (charSet == null) {
                    charSet = "UTF-8";
                }
                strPage = EntityUtils.toString(entity, charSet);
                strPage = strPage.replaceAll("\r", "");
                strPage = strPage.replaceAll("\n", "");
                strPage = strPage.replaceAll("&nbsp;", "");
            }
        } catch (Exception e) {
            connectionManager = null;
            httpParams = null;
            this.httpClient = null;
            this.errorHandler(url, e);
        } finally {
            try {
                httpGet.abort();
            } catch (Exception e) {
                log.error("Error when httpclient aborting " + e);
            }

            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    log.error("Error when consume entity ", e2);
                }
            }
        }
        return strPage;
    }

    public List<NameValuePair> getDefaultHeaders() {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        BasicNameValuePair nameValuePair = new BasicNameValuePair("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.add(nameValuePair);
        nameValuePair = new BasicNameValuePair("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
        headers.add(nameValuePair);
        nameValuePair = new BasicNameValuePair("Accept-Encoding", "GBK,utf-8;q=0.7,*;q=0.3");
        headers.add(nameValuePair);
        nameValuePair = new BasicNameValuePair("Accept-Language", "zh-CN,zh;q=0.8");
        headers.add(nameValuePair);
        nameValuePair = new BasicNameValuePair("Cache-Control", "max-age=0");
        headers.add(nameValuePair);
        nameValuePair = new BasicNameValuePair("Connection", "keep-alive");
        headers.add(nameValuePair);
        nameValuePair = new BasicNameValuePair("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19");
        headers.add(nameValuePair);
        return headers;
    }

    public String getTaobaoContent(List<NameValuePair> params) {
        return this.getPageContent(Constant.TAOBAO_API_URL, params);
    }

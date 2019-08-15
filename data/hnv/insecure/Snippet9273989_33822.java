public class RestClient
{

private ArrayList <NameValuePair> params;
private ArrayList <NameValuePair> headers;

private JSONObject jsonData;

private Object[] rtnData;

private String url;

private boolean connError;

public int getResponseCode() {
    return responseCode;
}

/**
 * 
 * @return  the result of whether the login was successful by looking at the response parameter of the JSON object. 
 */
public Boolean DidLoginSucceed()
{
    // Will Crash on socket error
        return ((JSONObject) rtnData[0]).optBoolean("response");
}

public String GetToken()
{
    return jsonData.optString("token");
}

public RestClient(String url)
{
    this.url = url;
    params = new ArrayList<NameValuePair>();
    headers = new ArrayList<NameValuePair>();
    rtnData = new Object[]{ new JSONObject() , Boolean.TRUE  };
}

public void AddParam(String name, String value)
{
    params.add(new BasicNameValuePair(name, value));
}

public void AddHeader(String name, String value)
{
    headers.add(new BasicNameValuePair(name, value));
}

/**
 * This method will execute, call the service and instantiate the JSON Object through executeRequest().
 * 
 * @param method    an enum defining which method you wish to execute.
 * @throws Exception
 */
public void ExecuteCall(RequestMethod method) throws Exception
{
    Object[] parameters = new Object[]{ new HttpGet() , new String("") };
    switch(method) {
        case GET:
        {
            //add parameters
            String combinedParams = "";
            if(!params.isEmpty()){
                combinedParams += "?";
                for(NameValuePair p : params)
                {
                    String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue());
                    if(combinedParams.length() > 1)
                    {
                        combinedParams  +=  "&" + paramString;
                    }
                    else
                    {
                        combinedParams += paramString;
                    }
                }
            }

            HttpGet request = new HttpGet(url + combinedParams);

            //add headers
            for(NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }
            parameters[0] = request;
            parameters[1] = url;

            new CallServiceTask().execute(parameters);

            jsonData = ((JSONObject) rtnData[0]).optJSONObject("data");
            connError = (Boolean) rtnData[1];
            break;

        }
        case POST:
        {
            HttpPost request = new HttpPost(url);

            //add headers
            for(NameValuePair h : headers)
            {
                request.addHeader(h.getName(), h.getValue());
            }

            if(!params.isEmpty()){
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }
            new CallServiceTask().execute(request, url);
            break;
        }
    }
}

private Object[] executeRequest(HttpUriRequest request, String url)
{
    HttpClient client = new DefaultHttpClient();
    client = getNewHttpClient();

    HttpResponse httpResponse;

    try {
        httpResponse = client.execute(request);
        HttpEntity entity = httpResponse.getEntity();

        if (entity != null) {

            InputStream instream = entity.getContent();
            String response = convertStreamToString(instream);
            try {
                rtnData[0] = new JSONObject(response);
                rtnData[1] = false;

            } catch (JSONException e1) {
                rtnData[1] = true;
                e1.printStackTrace();
            }

            // Closing the input stream will trigger connection release
            instream.close();
        }

    } catch (ClientProtocolException e)  {
        client.getConnectionManager().shutdown();
        e.printStackTrace();
    } catch (IOException e) {
        client.getConnectionManager().shutdown();
        e.printStackTrace();
    }
    return rtnData;
}


private static String convertStreamToString(InputStream is) {

    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return sb.toString();
}

/**
 * Custom HTTP Client accepting all SSL Certified Web Services.
 * 
 * @return n HttpClient object.
 */
public HttpClient getNewHttpClient() {
    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

        return new DefaultHttpClient(ccm, params);
    } catch (Exception e) {
        return new DefaultHttpClient();
    }
}

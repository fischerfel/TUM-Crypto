public class HTTPService {


    private static String response;


    public  HTTPService() {
        response = null;
    }


    public static String httpGet(String requestURL) {

        //Reset response's value to null
        response = null;
        //DEBUG LOG
        Log.i("HTTPService", "Start GET request with URL " + requestURL);

        //this function will do http getting action and response checking at same time.
        //and assign proper information into the response string.
        executeRequest(new HttpGet(requestURL));

        Log.i("HTTPService", "GET request return with: " + response);

        return response;
    }

    public static String httpPost(String requestURL, String postString) throws Exception {

        //Assemble POST request
        HttpPost request = new HttpPost(requestURL);
        StringEntity input = new StringEntity(postString);
        input.setContentType(CommonCodes.CONTENT_TYPE);
        request.setEntity(input);

        //DEBUG LOG
        Log.i("HTTPService", "Start POST request with URL" + requestURL + ", and post content " + postString);

        executeRequest(request);

        return response;

    }

    public static void httpPut(String requestURL, String postString) throws Exception {

        //Assemble PUT request
        HttpPut request = new HttpPut(requestURL);
        StringEntity input = new StringEntity(postString);
        input.setContentType(CommonCodes.CONTENT_TYPE);
        request.setEntity(input);

        //DEBUG LOG
        Log.i("HTTPService", "Start PUT request with URL" + requestURL + ", and put content " + postString);

        executeRequest(request);

    }

    public static String httpDelete(String requestURL) {

        HttpDelete request = new HttpDelete(requestURL);
        //DEBUG LOG
        Log.i("HTTPService", "Start DELETE request with URL" + requestURL);
        executeRequest(request);
        return response;

    }


    public static void executeRequest(HttpUriRequest request) {

        //Check Internet Connection
        if (!CheckInternetConnection.checkInternetConnection()) {
            responseValidation(CommonCodes.NO_CONNECTION, 0);
        }




        HttpClient client = sslHttpClient();
        HttpResponse httpResponse;

        try {


            //*********PLACE WHERE ERROR HAPPENS IN ERROR LOG!******//
            httpResponse = client.execute(request);

            //Check if the request success. If it success, should return 200 as status code.
            if (httpResponse.getStatusLine().getStatusCode() != CommonCodes.HTTP_SUCCESS) {
                //DEBUG LOG
                Log.i("HTTPService", "Request failed with HTTP status code" + httpResponse.getStatusLine().getStatusCode());
                //Go through checker
                responseValidation(null, httpResponse.getStatusLine().getStatusCode());
            }

            //When request succeed, retrieve data.
            //HttpEntity entity = httpResponse.getEntity();

            //Convert stream data into String.
            if (!httpResponse.getEntity().equals(null)) {

                InputStream instream = httpResponse.getEntity().getContent();

                //Convert InputStream to String.
                response = convertStreamToString(instream).trim();

                //Close stream
                instream.close();
                httpResponse.getEntity().consumeContent();
            }

            //Go through checker
            responseValidation(response, httpResponse.getStatusLine().getStatusCode());

            client.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {
            Log.e("HTTPService: ", "HTTP Request With ClientProtocolException Detected");
            client.getConnectionManager().shutdown();
            e.printStackTrace();

        } catch (IOException e) {
            Log.e("HTTPService: ", "HTTP Request With IOException Detected");
            client.getConnectionManager().shutdown();
            e.printStackTrace();

        }

    }



    public static void  responseValidation (String response, int statusCode)
    {
        //Is this a HTTP Error?
        if (statusCode != CommonCodes.HTTP_SUCCESS) {
            response = CommonCodes.HTTP_FAIL;
        }
        //Is the response contains nothing?
        else if (response.equals(null) || response.equals("")) {
            response = CommonCodes.RESPONSE_EMPTY;
        }
        //Is there an internet connection?
        else if (response.equals(CommonCodes.NO_CONNECTION)){
            response = CommonCodes.NO_CONNECTION;
        }

    }


    public static HttpClient sslHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocket(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 8080));
            registry.register(new Scheme("https", sf, 8443));


            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);


            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
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

public static void main(String args[]) throws ParseException,
        HttpException, IOException, SOAPException, NoSuchAlgorithmException {

    System.out.println("started");

    try {

        System.getProperties().put("http.proxyHost", "proxy.company.com");
        System.getProperties().put("http.proxyPort", "80");
        System.getProperties().put("http.proxyUser", "abcde");
        System.getProperties().put("http.proxyPassword", "********");

        SSLContext sslContext = SSLContext.getInstance("SSL");

        // set up a TrustManager that trusts everything
        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                System.out.println("getAcceptedIssuers =============");
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                    String authType) {
                System.out.println("checkClientTrusted =============");
            }

            public void checkServerTrusted(X509Certificate[] certs,
                    String authType) {
                System.out.println("checkServerTrusted =============");
            }
        } }, new SecureRandom());

        SSLSocketFactory sf = new SSLSocketFactory(sslContext);
        Scheme httpsScheme = new Scheme("https", 443, sf);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(httpsScheme);

        // apache HttpClient version >4.2 should use
        // BasicClientConnectionManager
        ClientConnectionManager cm = new BasicClientConnectionManager(
                schemeRegistry);
        DefaultHttpClient httpClient = new DefaultHttpClient(cm);

        HttpPost postRequest = new HttpPost(
                "https://login.salesforce.com/services/oauth2/token");

        String username = "------------";
        String password = "---------------";
        String client_id = "3MVGS";
        String client_secret = "894448";

        List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
        parametersBody
                .add(new BasicNameValuePair("grant_type", "password"));
        parametersBody.add(new BasicNameValuePair("username", username));
        parametersBody.add(new BasicNameValuePair("password", password));
        parametersBody.add(new BasicNameValuePair("client_id", client_id));
        parametersBody.add(new BasicNameValuePair("client_secret",
                client_secret));

        postRequest.setEntity(new UrlEncodedFormEntity(parametersBody));
        postRequest.setHeader("Content-Type",
                "application/x-www-form-urlencoded");

        HttpResponse response = httpClient.execute(postRequest);

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (response.getEntity().getContent())));

        JSONParser parser = new JSONParser();

        Object obj = parser.parse(br.readLine());

        JSONObject jsonObject = (JSONObject) obj;

        System.out.println(jsonObject.toString());

        String id = (String) jsonObject.get("id");
        System.out.println("ID: " + id);

        String access_token = (String) jsonObject.get("access_token");
        System.out.println("ACCESS_TOKEN: " + access_token);

        httpClient.getConnectionManager().shutdown();

    } catch (MalformedURLException e) {

        e.printStackTrace();

    } catch (IOException e) {

        e.printStackTrace();

    } catch (KeyManagementException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (org.json.simple.parser.ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}

public class MyHttpClient {

    private String cookies;

    private HttpClient client = getNewHttpClient() ;

    private final String USER_AGENT = "Chrome/39.0.2171.65";

    @SuppressLint("NewApi")


    /* this method is called from the
    * mainactivity .
    *courseUrl is the page i want to go after i login
    */
    public String getHtmlFromElearn(String courseURL) throws Exception{

        String url = "https://elearn.uoc.gr/login/index.php"; //the login page

        CookieHandler.setDefault(new CookieManager());

        MyHttpClient http = new MyHttpClient();

        String page = http.GetPageContent(url);  //gets the html code of login page

        List<NameValuePair> postParams =
               http.getFormParams(page, "mymail@csd.uoc.gr","mypass"); //pasing the email and pass params to post request

        http.sendPost(url, postParams); // sends the post request with the params found in form

        String result = http.GetPageContent(courseURL);


        System.out.println("Done");
        return result;
  }

    /*
    * sends post request with params to login to the website
    */
    private void sendPost(String url, List<NameValuePair> postParams) 
        throws Exception {
        //was getting exception for main thred without this
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpPost post = new HttpPost(url);

        // add header details
        post.setHeader("Host", "elearn.uoc.gr");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        post.setHeader("Accept-Language", "el-GR,el;q=0.8");
        post.setHeader("Cookie", getCookies());
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", "https://elearn.uoc.gr/login/index.php");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setEntity(new UrlEncodedFormEntity(postParams,HTTP.UTF_8));

        HttpResponse response = client.execute(post);
        // THE RESPONSE I AM GETTING IS 200
        int responseCode = response.getStatusLine().getStatusCode();

        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postParams);
        System.out.println("Response Code : " + responseCode);

        //copy the response page in to a string
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line).append("\n");
        }
        if(result.toString().contains("login failed")){ // checks if login succeeded
             System.out.println("login faild");
        }
        System.out.println(result.toString());


    }

    @SuppressLint("NewApi")
    /* 
    * gets the html code from the url given
    */
    private String GetPageContent(String url) throws Exception {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //httpget request to get the html of page
        HttpGet request = new HttpGet(url);

        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader("Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        request.setHeader("Accept-Language", "el-GR,el;q=0.8");
        HttpResponse response = client.execute(request);
        int responseCode = response.getStatusLine().getStatusCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        //copies response html code to string
        BufferedReader rd = new BufferedReader(
                   new InputStreamReader(response.getEntity().getContent()));

        String result = new String();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result+=line+"\n";
        }

        // set cookies
        setCookies(response.getFirstHeader("Set-Cookie") == null ? "" : 
                     response.getFirstHeader("Set-Cookie").toString());

        return result;

    }
    /*gets the email and pass and returns a lists withh all the paramas
     * for post request to login
     */
    public List<NameValuePair> getFormParams(
             String html, String username, String password)
            throws UnsupportedEncodingException {

        System.out.println("Extracting form's data...");

        Document doc = Jsoup.parse(html);


        Element loginform = doc.getElementById("login");
        Elements inputElements = loginform.getElementsByTag("input");

        List<NameValuePair> paramList = new ArrayList<NameValuePair>();

        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");

            if (key.equals("username")){
                value = username;

            }
            else if (key.equals("password")){
                value = password;

            }


            paramList.add(new BasicNameValuePair(key,  URLEncoder.encode(value, "UTF-8")));

        }

        return paramList;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }
    /* 
     *return an http client that trusts all cerfificates
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
}

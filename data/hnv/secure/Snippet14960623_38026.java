public class PostData extends AsyncTask<Void, String, Boolean> {
Context ourContext;

public PostData(Context c) {
    ourContext = c;
}

@Override
protected Boolean doInBackground(Void... arg0) {


    HttpPost httppost = new HttpPost(
            "https://online.lloydstsb.co.uk/personal/logon/login.jsp?WT.ac=hpIBlogon");
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
    nameValuePairs.add(new BasicNameValuePair("frmLogin:strCustomerLogin_userID", "<myuser>"));
    nameValuePairs.add(new BasicNameValuePair("frmLogin:strCustomerLogin_pwd", "<mypass>"));
    nameValuePairs.add(new BasicNameValuePair("frmLogin:btnLogin1", "Continue"));
    try {
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    try {
        HttpResponse response = getClient().execute(httppost);
        try {   


            if(response.getStatusLine().getStatusCode() == 200) {   

                HttpEntity entity = response.getEntity();
                System.out.println("Success");



            } else { 
                System.out.println("ERROR");
            }

        } catch (Exception e) {

        }


    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return true;
}

public DefaultHttpClient getClient() {
    DefaultHttpClient ret = null;

    // sets up parameters
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, "utf-8");
    params.setBooleanParameter("http.protocol.expect-continue", false);

    // registers schemes for both http and https
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory
            .getSocketFactory(), 80));
    final SSLSocketFactory sslSocketFactory = SSLSocketFactory
            .getSocketFactory();
    sslSocketFactory
            .setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    registry.register(new Scheme("https", sslSocketFactory, 443));

    ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(
            params, registry);
    ret = new DefaultHttpClient(manager, params);
    return ret;
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ok= (Button) findViewById(R.id.login);
    user= (EditText) findViewById(R.id.user);
    pass= (EditText) findViewById(R.id.pass);
    ok.setOnClickListener(new OnClickListener(){


        @Override
        public void onClick(View arg0) {

            trustEveryone();

            Connection.Response response = Jsoup.connect("https://uogis.uog.edu.pk/Default.aspx?You%20are%20logout%20now.%20Please%20login%20again!")
                    .method(Connection.Method.GET)
                    .execute();

            response = Jsoup.connect("https://uogis.uog.edu.pk/Default.aspx?You%20are%20logout%20now.%20Please%20login%20again!")
                    .data("txtLogin", user)
                    .data("txtPassword", pass)
                    .cookies(response.cookies())
                    .method(Connection.Method.POST)
                    .execute(); 

            Document homePage = Jsoup.connect("https://uogis.uog.edu.pk/LandingPage.aspx")
                    .cookies(response.cookies())
                    .get();




                System.out.print(homePage);


        }

    });
}
    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {


                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    // TODO Auto-generated method stub
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }



}

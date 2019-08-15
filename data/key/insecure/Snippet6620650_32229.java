    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String base_url="http://api.twitter.com/oauth/request_token";
    String oauth_callback="http://api.twitter.com/1/statuses/public_timeline.json";
    String oauth_consumer_key="GDdmIQH6jhtmLUypg82g";
    String oauth_consumer_secret="MCD8BKwGdgPHvAuvgvz4EQpqDAtx89grbuNMRd7Eh98&";
    String oauth_signature_method="HMAC-SHA1";


    int oauth_timestamp=1272323042;
    String oauth_version="1.0";
    String oauth_nonce="QP70eNmVz8jvdPevU3oJD2AfF7R7odC2XJcn4XlZJqk";

    String oauth_signature="";

    String signature_base_string = null;
    try {
        signature_base_string = "POST"
                + "&"
                + URLEncoder.encode(
                        "https://api.twitter.com/oauth/request_token",
                        "UTF-8")
                + "&"
                + URLEncoder
                        .encode("oauth_callback=http%3A%2F%2Flocalhost%3A3005%2Fthe_dance%2Fprocess_callback%3Fservice_provider_id%3D11&oauth_consumer_key=GDdmIQH6jhtmLUypg82g&oauth_nonce=QP70eNmVz8jvdPevU3oJD2AfF7R7odC2XJcn4XlZJqk&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1272323042&oauth_version=1.0",
                                "UTF-8");
        System.out.println(signature_base_string);
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    try {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(
                oauth_consumer_secret.getBytes(), "HmacSHA1");
        mac.init(secret);
        byte[] digest = mac.doFinal(signature_base_string.getBytes());

        BASE64Encoder encoder = new BASE64Encoder();
        oauth_signature=encoder.encode(digest);
        System.out.println(oauth_signature);

    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    URL url=new URL(base_url);

    HttpURLConnection httpURLCon=(HttpURLConnection)url.openConnection(MyProxy.getProxy());


    httpURLCon.setRequestMethod("POST");
    httpURLCon.setRequestProperty("Authorization", "OAuth "+"oauth_nonce='QP70eNmVz8jvdPevU3oJD2AfF7R7odC2XJcn4XlZJqk', oauth_callback='http%3A%2F%2Flocalhost%3A3005%2Fthe_dance%2Fprocess_callback%3Fservice_provider_id%3D11', oauth_signature_method='HMAC-SHA1', oauth_timestamp='1272323042', oauth_consumer_key='GDdmIQH6jhtmLUypg82g', oauth_signature='8wUi7m5HFQy76nowoCThusfgB%2BQ%3D', oauth_version='1.0'");
    httpURLCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:5.0) Gecko/20100101 Firefox/5.0");


    /*Map<String,List<String>> headers= httpURLCon.getHeaderFields();

    Iterator<String> i=headers.keySet().iterator();
    while(i.hasNext()){
        String key=i.next();
        System.out.println(key);
        System.out.println(headers.get(key).toString());
        System.out.println("====================");
    }
    */

    httpURLCon.connect();
    InputStream is=httpURLCon.getInputStream();
    byte[] b=new byte[1024*4];
    int read;
    while((read=is.read(b))!=-1){
        response.getOutputStream().write(b,0,read);
    }
}

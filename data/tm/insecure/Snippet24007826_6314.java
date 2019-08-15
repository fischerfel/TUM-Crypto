    public static String getTwitterFeed() {
     String retVal = "";

     try { 
     HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
         public boolean verify(String hostname, SSLSession session) { 
                 return true; 
         }}); 
     SSLContext context = SSLContext.getInstance("TLS");
     context.init(null, new X509TrustManager[]{new X509TrustManager(){
         public void checkClientTrusted(X509Certificate[] chain, 
                         String authType) throws CertificateException {} 
         public void checkServerTrusted(X509Certificate[] chain, 
                         String authType) throws CertificateException {} 
         public X509Certificate[] getAcceptedIssuers() { 
                 return new X509Certificate[0]; 
         }}}, new SecureRandom()); 
     HttpsURLConnection.setDefaultSSLSocketFactory(
                 context.getSocketFactory()); 
     } catch (Exception e) { // should never happen
     e.printStackTrace();
     }

     ConfigurationBuilder cb = new ConfigurationBuilder();
     cb.setDebugEnabled(true).setOAuthConsumerKey("XXXXXXXXXXXXXXXXXXXXXX").
     setOAuthConsumerSecret("XXXXXXXXXXXXXXXXXXXXXXXXXX").
     setOAuthAccessToken("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx").
     setOAuthAccessTokenSecret("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX").
     setHttpProxyHost("proxy.com").
     setHttpProxyPort(1111);

       try {
         TwitterFactory tf = new TwitterFactory(cb.build());
         Twitter twitter = tf.getInstance();
         AppModule am = new AppModule();
         String tag = am.getTwitterTags();
           if(tag == null || tag.length()==0) {
               System.out.println("no tag found");
               tag = "#rajneesh";
           }
         Query query = new Query(tag);
         query.setCount(5);
         QueryResult result;
         result = twitter.search(query);
         List<Status> tweets = result.getTweets();
         for (Status tweet : tweets) {
             String tweetText = twitterTemplate;
             tweetText = tweetText.replaceAll("~author~", tweet.getUser().getScreenName()).replaceAll("~Tweet",tweet.getText()).
                 replaceAll("~elapsed time~", tweet.getCreatedAt().toString());
             retVal = retVal+tweetText;
         }
     } catch (TwitterException te) {
         te.printStackTrace();
     } catch (Exception e) {
         e.printStackTrace();       
     }
     String html = "<html><head></head><body>" + retVal+"</body></html>";
    // System.out.println(html);
     return html;
 }

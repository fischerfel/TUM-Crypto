 // create URL Object from String
 URL url = new URL(https_url);
 // set IP Adress for X509TrustManager
 caTrustManager.setHostIP(url.getHost());     
 TrustManager[] trustCerts = new TrustManager[]{
     caTrustManager
 };
 SSLContext sc = SSLContext.getInstance("SSL");
 sc.init(null, trustCerts, new java.security.SecureRandom());
 //'ArrayList' where the data is saved from the URL
 ArrayList data = null;
 // open URL
 HttpsURLConnection httpsVerbindung = (HttpsURLConnection) url.openConnection();           
 httpsVerbindung.setSSLSocketFactory(sc.getSocketFactory());
 // Read the data from the URL
 data = PerformAction.getContent(httpsVerbindung);

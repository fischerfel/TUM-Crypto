  public void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
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
    }

Then call it before opening the URL.



    public void get() {
     try 
     { 
         trustEveryone();
         SAXParserFactory factory = SAXParserFactory.newInstance(); 
         SAXParser mSaxParser = factory.newSAXParser(); 
         XMLReader mXmlReader = mSaxParser.getXMLReader(); 
         mXmlReader.setContentHandler(this); 
         InputStream mInputStream = new URL(URL_MAIN).openStream();

         mXmlReader.parse(new InputSource(mInputStream));
     } 
     catch(Exception e) 
     { 
         Log.e(TAG, "Exception: " + e.getMessage()); 
     } 
 } 

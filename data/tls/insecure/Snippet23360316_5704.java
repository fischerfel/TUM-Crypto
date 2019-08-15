public void sendSoap(String endpoint) {
     HttpsURLConnection httpsUrlConnection = null;
     URL url = new URL(endpoint);
     httpsUrlConnection = (HttpsURLConnection) url.openConnection();
     httpsUrlConnection.setRequestMethod("POST");
     httpsUrlConnection.setDoOutput(true);
     httpsUrlConnection.setRequestProperty("Content-Type", "application/xml");
     SSLContext sc = SSLContext.getInstance("SSL");
     sc.init(null, getTrustManager(), new SecureRandom());
     httpsUrlConnection.setSSLSocketFactory(sc.getSecureSocketFactory());
     httpsUrlConnection.connect();
     try {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsUrlConnection.getOutputStream());
        outputStreamWriter.write(buildData());
        outputStreamWriter.flush();
        outputStreamWriter.close();
        InputStreamReader inputStreamReader = new InputStreamReader(httpsUrlConnection.getInputStream());
    }
    catch(Exception e) {
        InputStream inputStream = httpsUrlConnection.getErrorStream();
        inputStreamReader i = new InputStreamReader(inputStream);
        BufferedReader j = new BufferedReader(i);
        int read = j.read();
        while(read != -1) {
            String line = j.readLine();
            System.out.println(line);
        }
    }
   }

public String buildData() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(new String("<?xml version='1.0' encoding='utf-8'?>"));
        stringBuilder.append(new String("<soap:Envelope xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xs='http://www.w3.org/2001/XMLSchema' xmlns:ns='http://somenamespace.com'>"));
        stringBuilder.append(new String("<soap:Body>"));
        stringBuilder.append(new String("<ns:service>"));
        stringBuilder.append(new String("<ns:message>message</ns:message>"));
        stringBuilder.append(new String("<ns:username>username</ns:username>"));
        stringBuilder.append(new String("<ns:password>password</ns:password>"));
        stringBuilder.append(new String("</ns:service>"));
        stringBuilder.append(new String("</soap:Body>"));
        stringBuilder.append(new String("</soap:Envelope>");
    }

 public TrustManager[] getTrustManager() {
   TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
       public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {

       }

       public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
           System.out.println("AuthType : " + " " + string);
           for(int i = 0; i < xcs.length; i++) {
               System.out.println("\t" + xcs[i].getIssuerX500Principal().getName());
               System.out.println("\t" + xcs[i].getIssuerDN().getName());
           }
       }

       public X509Certificate[] getAcceptedIssuers() {
           return null;
       }
   }};
   return trustAllCerts;
 }

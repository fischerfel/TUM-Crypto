  TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
      public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        // TODO Auto-generated method stub

    }
    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        // TODO Auto-generated method stub

    }

  } };

  SSLContext sc = SSLContext.getInstance("SSL");
  sc.init(null, trustAllCerts, new java.security.SecureRandom());
  HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

  // Create all-trusting host name verifier
  HostnameVerifier allHostsValid = new HostnameVerifier() {
    @Override
    public boolean verify(String arg0, SSLSession arg1) {
        // TODO Auto-generated method stub
        return false;
    }
  };
  // Install the all-trusting host verifier
  HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    URL url = new URL(urlstring);
    System.out.println("thea here url"+url);

    HttpsURLConnection httpURLConnection = (HttpsURLConnection)url.openConnection();
    httpURLConnection.setDoInput(true);
    httpURLConnection.setRequestMethod("POST");
    httpURLConnection.setRequestProperty("Accept","*/*");
    httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
    httpURLConnection.setRequestProperty("content-type","application/x-www-form-urlencoded");
    String authString = encodedusername + ":" + encodedpasswd;
    System.out.println("auth string: " + authString);
    byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
    String authStringEnc = new String(authEncBytes);
    System.out.println("  Base64 encoded auth string: " + authStringEnc);
    httpURLConnection.setRequestProperty ("Authorization", "Basic "+authStringEnc);
    httpURLConnection.setRequestProperty("Content-length", "100");
    httpURLConnection.setDoOutput(true);
     OutputStream output = httpURLConnection.getOutputStream();
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(output,"UTF-8"),true); 
    System.out.println("httpURLConnection pw strin: " + pw.toString());
    pw.write("query="+query+"&partitionName="+partition+"&readOnly="+isReadOnly+"&maxRecords="+maxrecords);
    pw.close();
    System.out.println("httpURLConnection pw strin 2: " + pw.toString());

     BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8")); 
        System.out.println("BufferedReader: " + br);
        System.out.println("httpURLConnection string 3: " + httpURLConnection.getResponseCode());
       }

            String urlParameters =
            "PaymentOKURL=" + URLEncoder.encode("http://test.com", "UTF-8")
            + "&PaymentFailURL=" + URLEncoder.encode("https://test.com", "UTF-8")
            + "&AmountToPay=" + URLEncoder.encode("170000", "UTF-8")
            + "&AmountCurrency=" + URLEncoder.encode("EUR", "UTF-8")
            + "&PayToMerchant=" + URLEncoder.encode("1000000", "UTF-8")
            + "&Details1=" + URLEncoder.encode("Invoice 1000", "UTF-8")
            + "&Details2=" + URLEncoder.encode("123456789", "UTF-8")
            + "&MerchantName=" + URLEncoder.encode("TEST", "UTF-8")
            + "&CheckSumHeader=" +URLEncoder.encode("08PaymentOKURL,PaymentFailURL,AmountToPay,AmountCurrency,PayToMerchant,Details1,Details2,MerchantName,0303030303003", "UTF-8")
            + "&CheckSum=" + URLEncoder.encode("7d4192f39c50d8d87863005fbc44515d", "UTF-8");


    URL url = null;
    String testUrl="https://redirectApp.com";
    try {
        url = new URL(testUrl);
    } catch (MalformedURLException ex) {
        Logger.getLogger(TextServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
    HttpsURLConnection httpsCon = null;
    InputStream is = null;
    try {
        httpsCon = (HttpsURLConnection) url.openConnection();
        httpsCon.setHostnameVerifier(new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        httpsCon.setRequestMethod("POST");
        System.out.println("execute  httpsCon.setRequestMethod(\"POST\");");
        httpsCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        System.out.println("execute  httpsCon.setRequestProperty(\"Content-Type\", \"application/x-www-form-urlencoded\");");
        httpsCon.setRequestProperty("Content-Length", String.valueOf(urlParametersEtax.getBytes().length));
        System.out.println("execute  Content-Length");
        httpsCon.setRequestProperty("Accept-Charset", "UTF-8");
        System.out.println("execute Accept-Charset");
        httpsCon.setDoOutput(true);
        System.out.println("execute  httpsCon.setDoOutput(true);");
        httpsCon.setInstanceFollowRedirects(true);  //you still need to handle redirect manully.
        HttpsURLConnection.setFollowRedirects(true);
        // 

        //add reuqest header

        OutputStream os = httpsCon.getOutputStream();
        System.out.println("execute   OutputStream os = con.getOutputStream();");
        os.write(urlParameters.getBytes());
        System.out.println("execute   os.write(urlParameters.getBytes());");
        httpsCon.connect();
        System.out.println("execute  httpsCon.connect();");
        int code = httpsCon.getResponseCode();
        System.out.println("RESPONSE CODE " + code);
    } catch (IOException ex) {
        Logger.getLogger(TextServlet.class.getName()).log(Level.SEVERE, null, ex);
    }

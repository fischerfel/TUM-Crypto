     public  HttpsURLConnection setUpHttpsConnection(String urlString)
    {
        String HttpMessage="";
        int HttpResult=0;
        try
        {
            Log.i("status","inside method..");
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");

            // My CRT file that I put in the assets folder
            // I got this file by following these steps:
            // * Go to https://littlesvr.ca using Firefox
            // * Click the padlock/More/Security/View Certificate/Details/Export
            // * Saved the file as littlesvr.crt (type X.509 Certificate (PEM))
            // The MainActivity.context is declared as:
            // public static Context context;
            // And initialized in MainActivity.onCreate() as:

            context = PayMerchant.this;

           /* InputStream caInput = new BufferedInputStream(context.getAssets().open("core_tec.jks"));
           // InputStream caInput = new BufferedInputStream("/assets/core_tec.jks"));
            Certificate ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());*/







           // Log.i("ca", String.valueOf((X509Certificate) ca).getSubjectDN());

            // Create a KeyStore containing our trusted CAs
            //String keyStoreType = KeyStore.getDefaultType();
           // KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            //keyStore.load(null, null);
            //keyStore.setCertificateEntry("ca", ca);
            Log.i("here", "here");
            InputStream caInput = context.getAssets().open("coretec.bks");
            KeyStore keyStore = KeyStore.getInstance("BKS");
            String pass="test123";
            char[] keystorePassword=pass.toCharArray();
            keyStore.load(caInput, keystorePassword);
Log.i("here1", "here1");
            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            // Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL(urlString);
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization", "SYZK9LIO98QIQNQ27H6921fgRyt63FHIxrQP76m0hNYT6BZ7I:pB1g5XX3Hw58buPENR03ZM4Vgm7P");

                /*InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(in));*/

            JSONObject postjson = new JSONObject();
            postjson.put("acquirerCountryCode","356");
            postjson.put("acquiringBin","408972");
            postjson.put("amount","124.05");
            postjson.put("businessApplicationId","MP");
            postjson.put("city","KOLKATA");
            postjson.put("country","IND");
            postjson.put("idCode","CA-IDCode-77765");
            postjson.put("name","Visa Inc. USA-Foster City");
            postjson.put("feeProgramIndicator","123");
            postjson.put("localTransactionDateTime","2017-02-13T08:12:26");
            postjson.put("referenceNumber","REF_123456789123456789123");
            postjson.put("type","1");
            postjson.put("recipientName","Jasper");
            postjson.put("recipientPrimaryAccountNumber","4123640062698790");
            postjson.put("retrievalReferenceNumber","412770451035");
            postjson.put("secondaryId","123TEST");
            postjson.put("senderAccountNumber","4027290077881580");
            postjson.put("senderName","Jasper");
            postjson.put("senderReference"," ");
            postjson.put("systemsTraceAuditNumber","451035");
            postjson.put("transactionCurrencyCode","INR");
            postjson.put("transactionIdentifier","381228649430015");

            Log.i("json", String.valueOf(postjson));


            DataOutputStream localDataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
            localDataOutputStream.writeBytes(postjson.toString());
            localDataOutputStream.flush();
            localDataOutputStream.close();
            Log.i("connection", "connecting...");
            urlConnection.connect();
            Log.i("connection", "connected");

            HttpResult =urlConnection.getResponseCode();
            HttpMessage =urlConnection.getResponseMessage();
            if(HttpResult ==HttpURLConnection.HTTP_OK) {
                Log.i("resok", "resok");
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                res=sb.toString();
                Log.i("res", res);

            } else {
                Log.i("resnotok", "resnotok");
                Log.i("res", String.valueOf(HttpResult));
                Log.i("res", HttpMessage);



            }

            return urlConnection;
        }
        catch (Exception ex)
        {
            Log.e("error", "Failed to establish SSL connection to server: " + ex.toString());
            return null;
        }
    }

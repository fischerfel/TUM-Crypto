public static HashMap<String, Object> callSOAPServer(StringBuffer soap, String action) {
    HttpsURLConnection urlConnection = null;
    boolean download = true;
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = IsakApp.appContext.getResources().openRawResource(R.raw.thawte);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            URL url = new URL("whateverPage.com");

            urlConnection =
                    (HttpsURLConnection) url.openConnection();

            urlConnection.setSSLSocketFactory(context.getSocketFactory());


            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(20000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            urlConnection.setRequestProperty("SOAPAction", action);
            OutputStream reqStream = urlConnection.getOutputStream();
            reqStream.write(soap.toString().getBytes());

            InputStream resStream = urlConnection.getInputStream();


            byte[] data = new byte[1024 * 1024];

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int count = urlConnection.getContentLength();
            int total = 0;
            int size = 0;
            while ((count = resStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, count);
           }

            buffer.flush();
            String str = new String(buffer.toByteArray(), "UTF-8");
            System.out.println("--------");
            System.out.println(str);
            String sn = str.replace("&amp;", "AMP");
            String[] stringArray = sn.split("\\r?\\n");
            String soapNew = stringArray[1];
            byte[] bytes = soapNew.getBytes("UTF-8");
            HashMap<String, Object> xMap = new HashMap<String, Object>();
            xMap.put(IsakApp.STATUS, "true");
            xMap.put("soap", bytes);
            resStream.close();

            return xMap;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if( urlConnection != null) {
                urlConnection.disconnect();
            }
        }
 }

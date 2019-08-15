nnable Register_runnable = new Runnable(){
        @Override
        public void run() {
            EditText emailText = (EditText) findViewById(R.id.editText1regist);

            EditText pwText = (EditText) findViewById(R.id.editText2registpw);

            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            try {
                KeyStore keyStore = KeyStore.getInstance("BKS");
                InputStream in =  
                getResources().openRawResource(R.raw.ballooncardbks);
                keyStore.load(in, "".toCharArray());
                TrustManagerFactory tmf = 
                TrustManagerFactory.getInstance("X509");
                tmf.init(keyStore);

                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);

                String actionUrl = "https://app.ballooncard.com/api/client/register/format/json";
                URL url = new URL(actionUrl);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
             //   con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");

                con.setSSLSocketFactory(context.getSocketFactory());

                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Charset", "UTF-8");
                con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

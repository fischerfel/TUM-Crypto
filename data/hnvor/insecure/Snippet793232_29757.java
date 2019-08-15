private boolean postMessage(String message){
    try{ 
         String serverURLS = getRecipientURL(message);

         serverURLS = "https:\\\\abc.my.domain.com:55555\\update";

         if (serverURLS != null){
             serverURL = new URL(serverURLS);
         }

        HttpsURLConnection conn = (HttpsURLConnection)serverURL.openConnection();

        conn.setHostnameVerifier(new HostnameVerifier() { 
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        } 
        });

        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();

        OutputStreamWriter wr = new OutputStreamWriter(os);

        wr.write(message);

        wr.flush();

        if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK)
            return false;
        else
            return true;

    }

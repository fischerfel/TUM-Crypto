try{
            URL url = new URL(urlString);
            HttpsURLConnection.setDefaultHostnameVerifier(new FakeHostVerifier());

            TrustManager[] trustAllCerts = new TrustManager[]{
                     new X509TrustManager() {
                         public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                             Log.d("SSLDemo", "getAcceptedIssuers");
                             return null;
                         }
                         public void checkClientTrusted(
                             java.security.cert.X509Certificate[] certs, String authType) {
                             Log.d("SSLDemo", "Check Client Trusted");
                         }
                         public void checkServerTrusted(
                             java.security.cert.X509Certificate[] certs, String authType) {
                             Log.d("SSLDemo", "Check Server Trusted");
                         }
                     }
                 };


    SSLContext sc = SSLContext.getInstance("TLS"); //"TLS"
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            int port = 443;
            SSLSocketFactory factory = HttpsURLConnection.getDefaultSSLSocketFactory();
            socket = (SSLSocket)factory.createSocket(url.getHost(), port);
            socket.startHandshake();


            /**
             * Connection Method
             */
            String method = "GET";

            String os = method + " "+urlString+" HTTP/1.0\r\n";
            os += "Content-Length: 0";
            os += "\r\n\r\n";

            ((SSLWeb)this.caller).updateRequest(urlString, method);

            Log.i("TESTWEB", os);
            BufferedWriter wout = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            wout.write(os);
            wout.flush();
            wout.close();
            rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //*********     Not using thread
            StringBuffer buff = new StringBuffer();

            char[] buffer = new char[1024];

            while(rd.read(buffer) > -1) {

                buff.append(buffer);
                Log.i("TESTWEB", "read buffer :" + String.valueOf(buffer));

            }
            Log.i("TESTWEB", "read line :" + buff.toString());

            //**********

        }catch(Exception e){
            Log.e("TESTWEB", "Connecting error", e);
            e.printStackTrace();
        }

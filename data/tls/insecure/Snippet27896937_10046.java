07:48:31: ->Kreiranje trust manager-a --Trust Manager
07:48:31: ->keystore manager -- Keystore Manager
07:48:43:-> ssl context 
07:48:43: -> postavljanje konekcije --request parameters
07:48:43: ->slanje podataka -- sending data
07:49:00: -> primanje podataka -- receiving data
07:49:01: ->ispis podataka

public void callWS() {

        String soapMessage = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hz=\"http://www.hz....hr/\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<hz>"
                + "<!--You have a CHOICE of the next 3 items at this level-->"
                + "<hz>155232113</hz>"
                + "</hz>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";

        HttpsURLConnection connSSL = null;

        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar
                .getInstance().getTime());

        System.out.println(timeStamp + ": ->Kreiranje trust manager-a");

        TrustManager[] trustAll = { new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs,
                    String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs,
                    String authType) {
            }

            public boolean isServerTrusted(X509Certificate[] certs) {
                return true;
            }

            public boolean isClientTrusted(X509Certificate[] certs) {
                return true;
            }
        } };

        timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar
                .getInstance().getTime());

        System.out.println(timeStamp + ": ->keystore manager");

        try {

            KeyManagerFactory kmf = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());

            KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");

            ks.load(null, null);

            kmf.init(ks, null);
            // KeyManager[] myKms = { new MyX509KeyMananger(this.p12File,
            // this.privateKeyPassword) };

            timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar
                    .getInstance().getTime());

            System.out.println(timeStamp + ":-> ssl context");

            SSLContext sslcont = SSLContext.getInstance("SSL");

            // kmf.getKeyManagers()
            sslcont.init(kmf.getKeyManagers(), trustAll, null);
            SSLSocketFactory factory = sslcont.getSocketFactory();

            HttpsURLConnection.setDefaultSSLSocketFactory(factory);

            timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar
                    .getInstance().getTime());

            System.out.println(timeStamp + ": -> postavljanje konekcije");
        } catch (NoSuchAlgorithmException e) {
            // throw new
            // ConnectionManagerException("No Such Algorithm Exception: " +
            // e.getMessage());
            System.out.println(e.getLocalizedMessage());
        } catch (KeyManagementException e) {
            // throw new ConnectionManagerException("Key Management Exception: "
            // + e.getMessage());

            System.out.println(e.getLocalizedMessage());
        } catch (Exception e) {
            // throw new
            // ConnectionManagerException("MyX509KeyManager Exception: " +
            // e.getMessage());

            System.out.println(e.getLocalizedMessage());
        }
        String res = "";

        String url = "https:.....";
        String SOAPAction = "https:/operation";

        try {

            URL urlSSL = new URL(null, url);

            BufferedReader in;

            connSSL = (HttpsURLConnection) urlSSL.openConnection();
            connSSL.setRequestProperty("Content-Type",
                    "text/xml; charset=utf-8");
            connSSL.setRequestProperty("Content-Length",
                    String.valueOf(soapMessage.length()));
            connSSL.setRequestProperty("SOAP-Action", SOAPAction);
            connSSL.setRequestMethod("POST");
            connSSL.setDoOutput(true);
            connSSL.setConnectTimeout(5000);

            timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar
                    .getInstance().getTime());

            System.out.println(timeStamp + ": ->slanje podataka");

            /*
             * PrintWriter out = new PrintWriter(new BufferedWriter( new
             * OutputStreamWriter(connSSL.getOutputStream())));
             * out.println(soapMessage); out.flush();
             */

            DataOutputStream wr = null;
            wr = new DataOutputStream(connSSL.getOutputStream());

            wr.writeBytes(soapMessage);
            wr.flush();
            wr.close();

            timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar
                    .getInstance().getTime());

            System.out.println(timeStamp + ": -> primanje podataka");

            in = new BufferedReader(new InputStreamReader(
                    connSSL.getInputStream(), "UTF-8"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                res = res + inputLine;

            }
            in.close();
            connSSL.disconnect();

            timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar
                    .getInstance().getTime());

            System.out.println(timeStamp + ": ->ispis podataka");

            System.out.println(res);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

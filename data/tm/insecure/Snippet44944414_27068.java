protected Boolean doInBackground(Void... params) {
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    } };
    final SSLContext sc;
    try {
        sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
        e.printStackTrace();
    }

    String https_url = "https://www.prisonvoicemail.com/";

    URL url;
    try {
        url = new URL(https_url);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

        JSONObject array = print_https_cert(con);
        try {
            Log.d(LOG_TAG, array.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.success = true;
    } catch (IOException e) {
        e.printStackTrace();
    }
    return this.success;
}

private JSONObject print_https_cert(HttpsURLConnection con){
    BASE64Encoder base64Encoder;
    JSONObject certificateInfo = new JSONObject();

    if(con!=null){
        try {
            System.out.println("Response Code : " + con.getResponseCode());
            System.out.println("Cipher Suite : " + con.getCipherSuite());
            System.out.println("\n");

            try {
                Certificate[] certs = con.getServerCertificates();
                MessageDigest mg = MessageDigest.getInstance("SHa-256");

                Certificate originalCert = certs[0];
                byte[] originalPublicKey = mg.digest(originalCert.getPublicKey().getEncoded());
                base64Encoder = new BASE64Encoder();
                String originalKey = base64Encoder.encode(originalPublicKey);

                //Get ip address
                Document doc = Jsoup.connect("http://checkip.org/").get();
                String ip = doc.getElementById("yourip").select("h1").first().select("span").text();

                try {
                    certificateInfo.put("ip", ip);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject certificates = new JSONObject();
                int index = 1;
                if(!originalKey.equals("Z12bL041tx\/0Hb8fNmTsL7ztXex\/02WlawBoMWMFRmc=")){
                    for(Certificate cert : certs){
                        //Data to write to json
                        //Get serial number
                        X509Certificate x509Certificate = (X509Certificate) cert;
                        BigInteger serial = x509Certificate.getSerialNumber();
                        //Get public key in sha-256 base64
                        byte[] publicKey = mg.digest(cert.getPublicKey().getEncoded());
                        base64Encoder = new BASE64Encoder();
                        String key = base64Encoder.encode(publicKey);
                        //getIssuer
                        Principal issuer = x509Certificate.getIssuerDN();

                        JSONObject values = new JSONObject();
                        try {
                            values.put("serial", serial.toString(16));
                            values.put("public-key", key);
                            values.put("issuer", issuer);
                            certificates.put(String.valueOf(index), values);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        index++;
                    }
                }

                try {
                    certificateInfo.put("certificates", certificates);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(Certificate cert : certs){
                    byte[] publicKey = mg.digest(cert.getPublicKey().getEncoded());
                    base64Encoder = new BASE64Encoder();
                    String key = base64Encoder.encode(publicKey);
                    X509Certificate x509Certificate = (X509Certificate) cert;
                    BigInteger serial = x509Certificate.getSerialNumber();
                    System.out.println("Cert Public Key: " + key);
                    System.out.println("Cert Serial number: " + serial.toString(16));
                    System.out.println("\n");
                }

            } catch (NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    return certificateInfo;
}

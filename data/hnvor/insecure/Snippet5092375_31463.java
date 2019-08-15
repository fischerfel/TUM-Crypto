        URL url = new URL(ip);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        con.connect();
        System.out.println("Response Code : " + con.getResponseCode());

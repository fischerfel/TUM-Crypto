    BufferedReader in = null;
    HttpsURLConnection client=null;
    try {

        URL url = new URL(appendedUrl);
        client= (HttpsURLConnection) url.openConnection();
        client.setRequestMethod("GET");
        client.setRequestProperty("Auth-Token", token);
        client.setConnectTimeout(120000);
        client.setReadTimeout(120000);
        if(appendedUrl.contains("https")) {
            client.setSSLSocketFactory(certificatePinning(context).getSocketFactory());//for certificate pinning
            client.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    if (hostname.equals("YOUR-HOST-ADDRESS"))
                        return true;
                    else
                        return false;
                }
            });
        }
        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
            sb.append(line + NL);
        }
        in.close();

        String result = sb.toString();
        return new JSONObject(result);
    }
    finally {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

public static void main(String[] args) throws Exception{
        String testURL = "https://testapi.com";

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, null, null);

        try {
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslcontext);
            CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

            HttpGet httpget = new HttpGet(testURL);

            CloseableHttpResponse response = client.execute(httpget);
            System.out.println("Response Code (Apache): " + response.getStatusLine().getStatusCode());
        }
        catch (Exception e){
            System.err.println("Apache HTTP Client Failed");
            e.printStackTrace();
        }

        try {
            HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(testURL).openConnection();
            urlConnection.setSSLSocketFactory(sslcontext.getSocketFactory());
            urlConnection.connect();
            System.out.println("Response Code (URLConnection): " + urlConnection.getResponseCode());
        }
        catch (Exception e){
            System.err.println("HttpsURLConnection Failed");
            e.printStackTrace();
        }

    }

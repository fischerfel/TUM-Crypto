    String Verify_Mobile_URL ="https://www.sample.php";
                        try 
                        {

                            StringBuilder postDataBuilder = new StringBuilder();
                            postDataBuilder.append("param1").append("=").append("paramvalue");
                            postDataBuilder.append("&").append("param2").append("=").append("paramvalue");


                            byte[] postData = postDataBuilder.toString().getBytes();

                            // Hit the dm URL.

                            URL url = new URL(Verify_Mobile_URL);
                            HttpsURLConnection.setDefaultHostnameVerifier(new AllVerifier());
                            SSLContext sslContext = SSLContext.getInstance("TLS");
                            sslContext.init(null, new TrustManager[] { new AllTrustManager() }, null);
                            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();        
                            conn.setReadTimeout(60000);
                            conn.setConnectTimeout(35000);
                            conn.setDoOutput(true);
                            conn.setUseCaches(false);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            conn.setRequestProperty("Content-Length",Integer.toString(postData.length));

                            OutputStream out = conn.getOutputStream();
                            out.write(postData);
                            out.close();

                            int responseCode = conn.getResponseCode();
                            if(responseCode==200)
                            {
                                InputStream inputstream=conn.getInputStream();  
                                String result=streamToString(inputstream);   // here you will will get result from

                            }
                            catch(Exception e)
                            {
                            }







/**
     * This method convert inputstream to string
     * @param is - inputtream to be converted
     * @return String - converted string 
     */
    public static String streamToString(InputStream is)
    {
        DataInputStream din = new DataInputStream(is);
        StringBuffer sb = new StringBuffer();
        try {
            String line = null;
            while ((line = din.readLine()) != null) 
            {
                sb.append(line + "\n");
            }

        } 
        catch (Exception ex) 
        {}      

        finally 
        {
            try 
            {  if(is!=null)
                {
                    din.close();
                    is.close();
                }
            } 
            catch (Exception ex) 
            {}

        }
        return sb.toString();

    }
















public class AllTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return new X509Certificate[0];
    }

}





public class AllVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        // TODO Auto-generated method stub
        return true;
    }

}

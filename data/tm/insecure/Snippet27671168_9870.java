private class SSLConnect extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... values) {
        //String https_url = "https://www.google.com/";
        //String https_url = "https://192.168.0.106:52428/webserveradmin/preferences";
        String https_url = "https://home-pc:52428/webserveradmin/preferences/";
        String response;

        try {
            TrustManager[] tm = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        //return new X509Certificate[0];
                        return null;
                    }
                }
            };

            URL url;
            try {
                url = new URL(https_url);
            }
            catch (MalformedURLException e) {
                return "Error URL: " + e.getMessage();
            }

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            try {
                conn.setDefaultHostnameVerifier(new NullHostNameVerifier());
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, tm, new SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Basic " + Base64.encode("sa:sa".getBytes(), Base64.DEFAULT));
                conn.connect();

                InputStream in = conn.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                response = sb.toString();

            } catch (GeneralSecurityException e) {
                return "Error Security: " + e.getMessage();
            }
        }
        catch(Exception e){
            return "Error SSL: " + e.getMessage();
        }
        return response;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctxt, result, Toast.LENGTH_LONG).show();
    }
}

public class NullHostNameVerifier implements HostnameVerifier{
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

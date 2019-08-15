public class TestService {

    static {
        disableSSLVerification();
    }

    public static void main(String[] args) {

        String[] names = {"login","seq","password"};    
        String[] values = { "admin", "2811", "admin" }; 

        String url = "https://localhost:8844/login";

        try {
            httpPost(url, names, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String httpPost(String urlStr, String[] paramName, String[] paramVal) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        OutputStream out = conn.getOutputStream();
        Writer writer = new OutputStreamWriter(out, "UTF-8");
        for (int i = 0; i < paramName.length; i++) {
            writer.write(paramName[i]);
            writer.write("=");
            writer.write(URLEncoder.encode(paramVal[i], "UTF-8"));
            writer.write("&");
        }
        System.out.println("WRITER: " + writer);
        writer.close();
        out.close();

        if (conn.getResponseCode() != 200) {
            throw new IOException(conn.getResponseMessage());
        }

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();

        conn.disconnect();
        return sb.toString();
    }

    public static void disableSSLVerification() {

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

        } };

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };      
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);       
    }
}

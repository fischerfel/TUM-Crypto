public class ConnectWebApp {

public String makeConnection(String urlData) throws FileNotFoundException, IOException {

        String xmlOutput = "";
        String url = "";
        String tail = "Servlet_Name";
        String str1 = "";

        String SERVER = "xxxxxx";
        String SERVERPORT = "8443";

        url = "https://" + SERVER + ":" + SERVERPORT + "/Project_Folder/" + tail;

                try {
            URL connectUrl = new URL(url);
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            } };
            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpsURLConnection con = (HttpsURLConnection) connectUrl.openConnection();
            System.out.println(con.getResponseCode());
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(urlData);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                buffer.append(line);
            }
            if (buffer.toString().equals(null)) {

                System.exit(1);
            }

            System.err.println(buffer.toString());
            System.exit(0);

            wr.close();
            rd.close();

        } catch (Exception e) {
            System.err.println("Error while making connection ");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return str1;
    }

    public static void main(String[] args) throws IOException {
/* For SSL Certificate*/
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                if (hostname.equals("xxxxx")) {
                    System.out.println(hostname);
                    return true;
                }
                return false;
            }
        });
        String urlData = URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode("Run", "UTF-8");

        ConnectWebApp C = new ConnectWebApp();
        C.makeConnection(urlData);
    }}

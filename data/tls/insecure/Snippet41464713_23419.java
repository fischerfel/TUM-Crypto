public class SendClient {
    public static void main(String[] args) throws Exception {

    TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

        }
    };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    /*
    * end of the fix
    */
    URL url = new URL("https://localhost:8001");

    URLConnection con = url.openConnection();
    //HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
    con.setDoOutput(true);
    con.setDoInput(true);
    con.setUseCaches(false);

    JSONObject writeJsonObj = new JSONObject();

    //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
    //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(con.getOutputStream()));
    writeJsonObj.put("msg","Hello server!");
    System.out.println(writeJsonObj.toString());
    printWriter.println(writeJsonObj);
    printWriter.flush();
    /*String text = writeJsonObj.toString();
    writer.write(text);
    writer.flush();*/

    while (true) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        System.out.println("--prepare to receive message");

        String line = null;

        line = bufferedReader.readLine();
        System.out.println("read something from server");
        System.out.println(line);
        JSONObject readJsonObj = new JSONObject(line);

        System.out.println(readJsonObj.toString());

        if (readJsonObj.has("test")) {
            System.out.println("Send the second message to server");
            printWriter.println(writeJsonObj);
            printWriter.println();
            printWriter.flush();
        }
    }
    }
}

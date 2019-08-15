public void makeHttpsCall(URL url, File file, String outputpath){
HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, util.disableCertificateValidation(),
                new SecureRandom());
        HttpsURLConnection
                .setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        URLConnection connection = url.openConnection();
        HttpsURLConnection httpsConn = (HttpsURLConnection) connection;
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), Charset.forName("ISO-8859-1")));
        StringBuilder builder = new StringBuilder();
        String aux = "";
        while ((aux = in.readLine()) != null) {
            builder.append(aux);
        }
        String requestContent = builder.toString();
        in.close();
        System.out.println(requestContent);
        byte[] b = requestContent.getBytes(Charset.forName("UTF-8"));

        httpsConn.setConnectTimeout(15000);
        httpsConn.setRequestProperty("Content-Length",
                String.valueOf(b.length));
        httpsConn.setRequestProperty("Content-Type",
                "text/xml; charset=ISO-8859-1");
        httpsConn.setRequestMethod("POST");
        httpsConn.setDoOutput(true);
        httpsConn.setDoInput(true);
        httpsConn.setReadTimeout(15000);
        OutputStream out = httpsConn.getOutputStream();
        out.write(b);
        out.close();

        System.out.println("Before geinputstream");
        int statusCode = httpsConn.getResponseCode();

        System.out.println("Response code : " + statusCode);
        // System.out.println("Error stream has value");

        if (statusCode != 200) {
            BufferedReader br_err = null;

            br_err = new BufferedReader(new InputStreamReader(
                    httpsConn.getErrorStream()));
            String err_response = "";
            String one_char;
            while ((one_char = br_err.readLine()) != null) {
                err_response += one_char;
            }
            System.out.println("Error response : " + err_response);
            httpsConn.disconnect();
            JOptionPane.showMessageDialog(null, "Fault response received");
            util.writeFaultResponseTofile(httpsConn.getContentType(),
                    outputpath, err_response, file.getName(), logger);
        }

        else {

            InputStreamReader isr = new InputStreamReader(
                    httpsConn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String responseString = "";
            String reponseContent = "";
            // Write the SOAP message response to a String.
            while ((responseString = br.readLine()) != null) {
                reponseContent = reponseContent + responseString;
            }

            if (httpsConn.getResponseMessage().equalsIgnoreCase("ok")) {
                logger_req.info("Response message OK ");
                httpsConn.disconnect();
                logger_req.info("Response content:" + reponseContent);

                util.writeResponseTofile(httpsConn.getContentType(),
                        outputpath, reponseContent, file.getName(), logger);

            } else {
                logger_req.info("Response message not OK ");
                JOptionPane.showMessageDialog(null,
                        "Response message from SSB is not OK");
            }
}

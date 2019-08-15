private String sendHTTPrequest(String uri, HttpRequestMethods method,
        String user, String password, int timeout, String body)
        throws Exception {
    if (sxtm != null) {
        URL url = new URL(uri);
        HttpsURLConnection httpsRequest = (HttpsURLConnection) url
                .openConnection();
        httpsRequest.setSSLSocketFactory(sxtm.getSSLInstance()
                .getSocketFactory());
        httpsRequest.setHostnameVerifier(sxtm.getHostInstance());
        httpsRequest.setRequestMethod(""+method);
        httpsRequest.setConnectTimeout(timeout);
        httpsRequest.setDoInput(true);
        httpsRequest.setDoOutput(true);
        if (useDigist) {
            // Currently not in use
            setDigestAuthentication(httpsRequest, user, password);
        } else {
            String authString = user + ":" + password;
            byte[] authEncBytes = Base64Coder.encode(authString
                    .getBytes());
            String authStringEnc = new String(authEncBytes);
            httpsRequest.addRequestProperty("Authorization", "Basic "
                    + authStringEnc);
        }

        System.out.println(httpsRequest.getResponseCode());

        if (httpsRequest.getContentType() != null
                && httpsRequest.getContentType().contains("xml")) {
            DocumentBuilderFactory dfactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = dfactory.newDocumentBuilder();
            lastXmlContent = builder.parse(httpsRequest.getInputStream());
            return httpsRequest.getResponseMessage();
        } else {
            return "No XML found";
        }
    }
    return "TrustManager is null";
}

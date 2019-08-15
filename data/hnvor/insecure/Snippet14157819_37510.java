try {
            String encoding = session.getPlatform().getEncodingIse();
            if (TradeUtil.isApachiClientActive()) {
                HttpResponse hr = XHttpClientFactory.getConnectionResponse(session.getPlatform(), TradeServiceType.ISE, command);
                if (!session.getPlatform().isUseStaticEncodingForIse()) {
                    encoding = XHttpClientFactory.getEncoding(hr, encoding);
                }
                strRtn = TradeUtil.getResponse(hr.getEntity().getContent(), encoding);
            } else {
                con = TradeUtil.createConnection(session.getPlatform(), TradeServiceType.ISE, command);
                if (!session.getPlatform().isUseStaticEncodingForIse()) {
                    encoding = TradeUtil.getEncoding(con, session.getPlatform().getEncodingIse());
                }
                strRtn = TradeUtil.getResponse(con.getInputStream(), encoding);
            }
        } catch (Exception e) {
            TradeUtil.toConsole(e);
            response.getResponseResult().setResponseCode(ResponseResult.CONNECTION_ERROR);
            response.getResponseResult().setDescription(e.getMessage());
            try {
                if (con != null)
                    con.disconnect();
            } catch (Exception ex) {
            }
            return response;
        }


   public static HttpURLConnection createConnection(Platform platform,
        TradeServiceType serviceType, String command) throws Exception {

    command = command + "&MTXTimeAnd=" + Calendar.getInstance().getTimeInMillis(); // Cachlemeyi
                                                                                    // Engellemek
                                                                                    // için

    TradeUtil.initSSLContext(); // Zoom ve ustundeki makinalar için SSL
                                // handshake için trust manager set etmek
                                // gerekiyor.
    System.setProperty("http.keepAlive", "false");// Yatirim finansman +
                                                    // Android 2.2 Bug için
                                                    // gerekli
    System.setProperty("https.keepAlive", "false"); // Yatirim finansman +
                                                    // Android 2.2 Bug için
                                                    // gerekli

    HttpURLConnection con;
    String methodType;
    String encoding;
    String strAdd = null;

    if (serviceType.equals(TradeServiceType.ISE)) {
        strAdd = platform.getIseServiceAdress();
        methodType = platform.getIseConnectionMethodType();
        encoding = platform.getEncodingIse();

    } else if (serviceType.equals(TradeServiceType.TURKDEX)) {
        strAdd = platform.getTurkdexServiceAdress();
        methodType = platform.getTurkdexConnectionMethodType();
        encoding = platform.getEncodingTurkdex();

    } else {
        throw new IllegalArgumentException("hatali service tipi");
    }
    if (methodType.equalsIgnoreCase("GET")) {
        strAdd = strAdd + "?" + command;
    }

    URL adress = new URL(strAdd);
    if (adress.getProtocol().equalsIgnoreCase("https")) {
        con = (HttpsURLConnection) adress.openConnection();
        ((HttpsURLConnection) con).setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    } else {
        con = (HttpURLConnection) adress.openConnection();
    }
    con.setUseCaches(false);
    con.setReadTimeout(platform.getConnectionTimeout());
    con.setConnectTimeout(platform.getConnectionTimeout());

    if (methodType.equalsIgnoreCase("POST")) {
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");

        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(con.getOutputStream(), encoding);
            osw.write(command);
            osw.flush();
            osw.close();
            osw = null;
        } catch (Exception ex) {
            TradeUtil.toConsole(ex);
            try {
                if (osw != null)
                    osw.close();
            } catch (Exception ex2) {
                TradeUtil.toConsole(ex);
            }
            throw ex;
        }

    } else {

    }
    return con;

}

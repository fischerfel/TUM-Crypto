        HttpURLConnection con = null;
        final BufferedReader rd = null;
        final BufferedOutputStream wr = null;
        final InputStreamReader isr = null;
        ServerConnectionData serverConnectionData = null;
        try {
                serverConnectionData = (ServerConnectionData) connectionData;
                final String urlPath = serverConnectionData.getUrlPath();
                final URL url = new URL(urlPath);
                trust();

                HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
                final URLConnection urlConnection = url.openConnection();
                con = (HttpURLConnection) urlConnection;
                con.setRequestMethod("POST");
                // con.addRequestProperty("Connection", "Keep-Alive");
                // con.addRequestProperty("Connection", "Close");                   

                if (serverConnectionData.getJsonData() != null) {
                    con.addRequestProperty(
                            "Content-Length",
                            Integer.toString(serverConnectionData.getJsonData().getBytes(
                                    Charset.forName("UTF8")).length));
                }
                if (serverConnectionData.getAdditionalHeaders() != null) {
                    final Map<String, String> additionalHeaders =
                            serverConnectionData.getAdditionalHeaders();
                    final Set<String> keySet = additionalHeaders.keySet();
                    for (final Object key : keySet) {
                        con.addRequestProperty((String) key, additionalHeaders.get(key));
                    }
                }

                final String authToken = "Bearer " +  "my outh token";
                con.addRequestProperty("Authorization", authToken);
                con.addRequestProperty("Content-Type", "application/json; charset=UTF-8");

                con.setUseCaches(false);
                con.setConnectTimeout(60000);
                con.setDoInput(true);
                con.setDoOutput(true);

                con.connect();

                sendData(con, serverConnectionData);

                responseCode = con.getResponseCode();
                responseText = getResponseText(con);

                if (responseCode == 200) {
                    // success
                } else {
                    // failure                      }
                }

        } catch (final MalformedURLException e) {
            throw new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR, e.getStackTrace());
        } catch (final IOException e) {
            Log.e(e.toString());
            try {
                if (null != con) {
                    responseCode = con.getResponseCode();
                    responseText = getResponseText(con);
                }
                if (responseCode == 401) {
                    // handle 401 response
                } else {
                    final ConnectionException ce =
                            new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR,
                                    e.getStackTrace());
                    ce.setResponseCode(responseCode);
                    throw ce;
                }
            } catch (final UnsupportedEncodingException uee) {
                throw new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR,
                        uee.getStackTrace());
            } catch (final IOException ioe) {
                throw new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR,
                        ioe.getStackTrace());
            }

        } catch (final KeyManagementException e) {
            throw new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR, e.getStackTrace());
        } catch (final NoSuchAlgorithmException e) {
            throw new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR, e.getStackTrace());
        } catch (final Exception e) {
            throw new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR, e.getStackTrace());
        } finally {
            if (wr != null) {
                try {
                    wr.flush();
                    wr.close();
                } catch (final IOException e) {
                    throw new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR,
                            e.getStackTrace());
                }

            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (final IOException e) {
                    throw new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR,
                            e.getStackTrace());
                }

            }
            if (rd != null) {
                try {
                    rd.close();
                } catch (final IOException e) {
                    throw new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR,
                            e.getStackTrace());
                }

            }
            if (con != null) {
                con.disconnect();
            }
        }


private void trust() throws NoSuchAlgorithmException, KeyManagementException {

    final TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(final java.security.cert.X509Certificate[] certs,
                    final String authType) {
            }

            @Override
            public void checkServerTrusted(final java.security.cert.X509Certificate[] certs,
                    final String authType) {
            }
        }
    };

    final SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

}

public void sendData(final HttpURLConnection con, final ServerConnectionData serverConnectionData)
        throws IOException, ConnectionException {

    final String requestJSON = serverConnectionData.getJsonData();
    if (requestJSON != null) {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(con.getOutputStream());
            bufferedOutputStream.write(requestJSON.getBytes());
            bufferedOutputStream.flush();
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                } catch (final IOException e) {
                    setOnlineStatus(ONLINE_STATUS_BAD);
                    throw new ConnectionException(ErrorCodes.SERVER_CONNECTION_ERROR,
                            e.getStackTrace());
                }

            }
        }
    }
}

 public String getResponseText(final HttpURLConnection con) throws IOException {

    InputStream stream = null;
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;
    String responseText = null;
    try {
        final int responseCode = con.getResponseCode();
        if (200 == responseCode) {
            stream = con.getInputStream();
        }
        if (null == stream) {
            stream = con.getErrorStream();
        }
        if (null == stream) {
            return "";
        }
        inputStreamReader = new InputStreamReader(stream, "UTF8");
        bufferedReader = new BufferedReader(inputStreamReader);
        final StringBuilder sb = new StringBuilder("");
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        responseText = sb.toString();
    } finally {
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (stream != null) {
            stream.close();
        }
    }
    return responseText;
}

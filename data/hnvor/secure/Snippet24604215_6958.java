private Response sendRequest(Request request, boolean debug)
            throws Exception {

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv =
                    HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("my.gov.uz", session);
            }
        };

        HttpsURLConnection conn = null;

        Response response = null;
        long time = 0;

        try {

            conn = (HttpsURLConnection) request.getUrl().openConnection();
            conn.setHostnameVerifier(hostnameVerifier);

            if (request.headers != null) {
                for (String header : request.headers.keySet()) {
                    conn.addRequestProperty(header, request.headers.get(header));
                }
            }

            time = System.currentTimeMillis();

            conn.setDoOutput(false);
            if (request instanceof POST) {
                byte[] payload = ((POST) request).body;

                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(payload.length);
                conn.getOutputStream().write(payload);
            }

            int status = conn.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)
                response = new Response(status, conn.getResponseMessage()
                        .getBytes());
            else {
                if (conn.getContentType().contains("application/json")) {
                    response = new Response(status, readInputStream(
                            conn.getInputStream()).getBytes());
                } else {
                    response = new Response(status,
                            readInputStreamWithoutUTF8(conn.getInputStream()));
                }

            }
            response.contentType = conn.getContentType();
            response.contentLength = conn.getContentLength();
            response.time = System.currentTimeMillis() - time;
            if (debug)
                dumpRequest(request, response);

        } catch (IOException e) {
            e.printStackTrace(System.err);
            throw e;

        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return response;
    }

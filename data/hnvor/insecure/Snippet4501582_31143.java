/**
     * Get the InputStream contents for a specific URL request, with parameters.
     * Uses POST. PLEASE NOTE: You should NOT use this method in the main
     * thread.
     * 
     * @param url
     *            is the URL to query
     * @param parameters
     *            is a Vector with instances of String containing the parameters
     */
    public static InputStream readHTTPContents(String url, String requestMethod, byte[] bodyData, String bodyEncoding, Map<String, String> parameters)
            throws AppException {
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            URL urlObj = new URL(url);
            if (urlObj.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) urlObj
                        .openConnection();
                https.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                connection = https;
            } else {
                connection = (HttpURLConnection) urlObj.openConnection();
            }
            // Allow input
            connection.setDoInput(true);
            // If there's data, prepare to send.
            if (bodyData != null) {
                connection.setDoOutput(true);
            }
            // Write additional parameters if any
            if (parameters != null) {
                Iterator<String> i = parameters.keySet().iterator();
                while (i.hasNext()) {
                    String key = i.next();
                    connection.addRequestProperty(key, parameters.get(key));
                }
            }
            // Sets request method
            connection.setRequestMethod(requestMethod);
            // Establish connection
            connection.connect();
            // Send data if any

            if (bodyData != null) {
                OutputStream os = connection.getOutputStream();
                os.write(bodyData);
            }
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new AppException("Error HTTP code " + connection.getResponseCode());
            }
            is = connection.getInputStream();
            int numBytes = is.available();
            if (numBytes <= 0) {
                closeInputStream(is);
                connection.disconnect();
                throw new AppException(MessageConstants.MSG_ERROR_CONNECTION_UNKNOWN);
            }

            ByteArrayOutputStream content = new ByteArrayOutputStream();

            // Read response into a buffered stream
            int readBytes = 0;
            while ((readBytes = is.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }
            ByteArrayInputStream byteStream = new ByteArrayInputStream(content.toByteArray());
            content.flush();
            return byteStream;
        } catch (Exception e) {
//          Logger.logDebug(e.getMessage());
            throw new AppException(e.getMessage());
        } finally {
            closeInputStream(is);
            closeHttpConnection(connection);
        }
    }

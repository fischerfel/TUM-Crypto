public class MultipartUtility {

    FileUploadListener listener;
    private static final int BUFFER_SIZE = 1024;
    private static final int TIME_OUT = 3 * 60 * 1000;
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;
    public int statusCode;
    public String mURL;

    public interface FileUploadListener {
        void onUpdateProgress(int percentage, long kb);

        boolean isCanceled();
    }

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset, FileUploadListener listener)
            throws IOException {
        this.charset = charset;
        this.listener = listener;
        mURL = requestURL;
        // creates a unique boundary based on time stamp
        boundary = "" + System.currentTimeMillis() + "";

        URL url = new URL(requestURL);
        httpConn = null;
        if (url.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            httpConn = https;
        } else {
            httpConn = (HttpURLConnection) url.openConnection();
        }

        // httpConn.setConnectTimeout(TIME_OUT);
        //httpConn.setReadTimeout(TIME_OUT);
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setChunkedStreamingMode(BUFFER_SIZE);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    private long lastProgressUpdateTime = 0;

    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        outputStream.flush();
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            final FileInputStream inputStream = new FileInputStream(uploadFile);
            long totalRead = 0;
            long totalSize = uploadFile.length();

            int read;
            while ((read = inputStream.read(buffer)) > 0) {
                totalRead += read;
                int percentage = (int) ((totalRead / (float) totalSize) * 100);
                outputStream.write(buffer, 0, read);

                long now = System.currentTimeMillis();
                if (lastProgressUpdateTime == 0 || lastProgressUpdateTime < now - 100) {
                    lastProgressUpdateTime = now;

                    Log.e("", totalRead + " " + " " + percentage);

                    if (listener != null)
                        this.listener.onUpdateProgress(percentage, totalRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outputStream.flush();
        }

        writer.append(LINE_FEED);
        writer.flush();
    }


    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String Execute() throws IOException {
        String responses = "";

        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();
        StringBuilder sb = new StringBuilder();
        try {
            // checks server's status code first
            statusCode = httpConn.getResponseCode();
            if (Constants.IS_DEBUG) {
                Log.v(Constants.TAG, "Url: " + mURL);
                Log.e("", "response code :" + statusCode);
            }
            //responses = ;

            sb.append("" + Utility.convertStreamToString(httpConn.getInputStream()) + "\n");

            if (statusCode == HttpURLConnection.HTTP_OK) {
                httpConn.disconnect();
            }
            responses = sb.toString();
            Log.v(Constants.TAG, " response: " + responses);
            return responses;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb = new StringBuilder();

            sb.append("" + Utility.convertStreamToString(httpConn.getErrorStream()) + "\n");

            responses = sb.toString();
            Log.v(Constants.TAG, "Error response: " + responses);
        }
        return responses;
    }
}

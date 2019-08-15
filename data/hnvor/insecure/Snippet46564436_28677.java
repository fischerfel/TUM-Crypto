import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MultipartUtility {
    FileUploadListener listener;
    private static final int BUFFER_SIZE = 4096;
    private static final int TIME_OUT = 3 * 60 * 1000;
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private static HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;
    public int statusCode;
    public String mURL;
    LinkedHashMap<String, String> mapRequest = new LinkedHashMap<>();

    Context _context;

    public interface FileUploadListener {
        void onUpdateProgress(int percentage, long kb);

        boolean isCanceled();

        void onException(String error);
    }

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset, String MethodName, FileUploadListener listener, Context _context)
            throws IOException {
        this.charset = charset;
        this.listener = listener;
        mURL = requestURL;
// creates a unique boundary based on time stamp
        boundary = "" + System.currentTimeMillis() + "";
        this._context = _context;
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

// httpConn.setRequestProperty("Token", Preferences.getPreference(GrowMovieApplication.mAppInstance, PrefEntity.KEY_TOKEN));
        if (MethodName.equalsIgnoreCase("POST")) {
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setRequestMethod("POST");
            httpConn.setDoInput(true);
            httpConn.setChunkedStreamingMode(BUFFER_SIZE);

            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
        } else if (MethodName.equalsIgnoreCase("GET")) {
            httpConn.setRequestMethod("GET");
        }


        httpConn.setRequestProperty("Connection", "Keep-Alive");
// httpConn.setRequestProperty(ServiceConstants.TENANT_GUID, ServiceConstants.REQ_HEADER_PARAM_VALUE_TENANTID_NEW);

        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
    }

    // For Support SSL Https
    public static void trustAllHosts() {
// Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
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
        writer.append(LINE_FEED);
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        // writer.append(value).append(LINE_FEED);
        writer.append(value);
        writer.flush();
        mapRequest.put(name, value);
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    private long lastProgressUpdateTime = 0;

    /*public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; Filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
// writer.append("charset=" + charset).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
        mapRequest.put(fieldName, uploadFile.getAbsolutePath());

        outputStream.flush();
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            final FileInputStream inputStream = new FileInputStream(uploadFile);
            long totalRead = 0;
            long totalSize = uploadFile.length();

            int read;
            while ((read = inputStream.read(buffer)) > 0) {
                totalRead += read;
                int percentage = (int) ((totalRead / (float) totalSize) * 10);
                outputStream.write(buffer, 0, read);

                long now = System.currentTimeMillis();
                if (lastProgressUpdateTime == 0 || lastProgressUpdateTime < now - 10) {
                    lastProgressUpdateTime = now;

                    Log.e(MultipartUtility.class.getName(), totalRead + " " + " " + percentage);

                    if (listener != null && percentage >= 1)
                        this.listener.onUpdateProgress(percentage, totalRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null)
                outputStream.flush();
        }

        writer.append(LINE_FEED);
        writer.flush();
    }*/

    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; Filename=\"" + fileName + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
         writer.append("charset=" + charset).append(LINE_FEED);
       // writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
        mapRequest.put(fieldName, uploadFile.getAbsolutePath());

        outputStream.flush();
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            final FileInputStream inputStream = new FileInputStream(uploadFile);
            long totalRead = 0;
            long totalSize = uploadFile.length();

            int read;
            while ((read = inputStream.read(buffer)) > 0) {
                totalRead += read;
                int percentage = (int) ((totalRead / (float) totalSize) * 10);
                outputStream.write(buffer, 0, read);

                long now = System.currentTimeMillis();
                if (lastProgressUpdateTime == 0 || lastProgressUpdateTime < now - 10) {
                    lastProgressUpdateTime = now;

                    Log.e(MultipartUtility.class.getName(), totalRead + " " + " " + percentage);

                    if (listener != null && percentage >= 1)
                        this.listener.onUpdateProgress(percentage, totalRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null)
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
        mapRequest.put(name, value);
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
        String codeWithResponses = "0";
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();
        StringBuilder sb = new StringBuilder();
        try {
// checks server's status code first
            statusCode = httpConn.getResponseCode();
// if (Constants.IS_DEBUG) {
            Log.i(MultipartUtility.class.getName(), "Url: " + mURL);
            Log.i(MultipartUtility.class.getName(), "response code :" + statusCode);

//Request params
            for (final String key : mapRequest.keySet()) {
/* print the key */
                Log.i(MultipartUtility.class.getName(), "PARAMS : " + key + " : " + mapRequest.get(key));
            }
// }
            sb.append(convertStreamToString(httpConn.getInputStream()) + "\n");

            if (statusCode == HttpURLConnection.HTTP_OK) {
                httpConn.disconnect();
            }
            responses = sb.toString();
            Log.v(MultipartUtility.class.getName(), " response: " + responses);
            return String.valueOf(httpConn.getResponseCode());
        } catch (Exception e) {
            sb = new StringBuilder();
            if (httpConn.getErrorStream() != null)
                sb.append(convertStreamToString(httpConn.getErrorStream()) + "\n");
            responses = sb.toString();
            Log.e(MultipartUtility.class.getName(), "Error response: " + responses);

            codeWithResponses = String.valueOf(statusCode);//new line
            if (listener != null)
                listener.onException(responses);
        }
        return codeWithResponses;
// return responses;
    }

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    // HTTP GET request
    public static String ExecuteGetRequest(String requestURL) throws Exception {

        String responses = "";

        URL obj = new URL(requestURL);

        httpConn = null;
        if (obj.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) obj.openConnection();
            https.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            httpConn = https;
        } else {
            httpConn = (HttpURLConnection) obj.openConnection();
        }

// httpConn.setRequestProperty("API-Key", API.HEADER_API_KEY);
// httpConn.setRequestProperty("Token", Preferences.getPreference(GrowMovieApplication.mAppInstance, PrefEntity.KEY_TOKEN));
        httpConn.setRequestProperty("Connection", "Keep-Alive");
// optional default is GET
        httpConn.setRequestMethod("GET");

        httpConn.connect();
        int responseCode;

        StringBuilder sb = new StringBuilder();
        try {
// checks server's status code first
            responseCode = httpConn.getResponseCode();

            Log.v(MultipartUtility.class.getName(), "Url: " + obj);
            Log.e(MultipartUtility.class.getName(), "response code :" + responseCode);
            sb.append("" + convertStreamToString(httpConn.getInputStream()) + "\n");

            if (responseCode == HttpURLConnection.HTTP_OK) {
                httpConn.disconnect();
            }
            responses = sb.toString();
            Log.v(MultipartUtility.class.getName(), " response: " + responses);
            return responses;
        } catch (Exception e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
            sb = new StringBuilder();

            sb.append("" + convertStreamToString(httpConn.getErrorStream()) + "\n");

            responses = sb.toString();
            Log.v(MultipartUtility.class.getName(), "Error response: " + responses);
        }
        return responses;
    }

}

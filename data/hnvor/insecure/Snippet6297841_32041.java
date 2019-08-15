public class C2DMTest {


    public static void main(String... args) throws Exception {
        String auth = authorize();
        if (auth == null) {
            System.out.println("No authorization returned");
            System.exit(1);
        }
        sendMessage(auth);
    }

    /**
     * Perform an authorization request to access Google's C2DM
     * API.
     *
     * @return The retrieved authorization request.
     */
    private static String authorize() throws Exception {
        String accountType = "GOOGLE";
        String service = "ac2dm";

        String source = "replace-me";
        String email = "replace-me";
        String passwd = "replace-me";


        StringBuilder params = new StringBuilder();
        params.append("accountType=").append(accountType)
                .append("&Email=").append(URLEncoder.encode(email, UTF8))
                .append("&Passwd=").append(URLEncoder.encode(passwd, UTF8))
                .append("&service=").append(service)
                .append("&source=").append(source);
        byte[] postData = params.toString().getBytes(UTF8);

        URL url = new URL("https://www.google.com/accounts/ClientLogin");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postData.length));

        //------------------
        OutputStream out = conn.getOutputStream();
        //--------------------------
        out.write(postData);
        out.close();

        int sw = conn.getResponseCode();
        System.out.println("" + sw);

        switch (sw) {
            case 503:
                System.out.println("Service unavailable");
                break;
            case 401:
                System.out.println(" Invalid authentication token");
                break;
            default:
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream in = conn.getInputStream();
                byte[] bytes = new byte[100];
                int len = -1;
                while ((len = in.read(bytes)) != -1) {
                    baos.write(bytes, 0, len);
                }
                in.close();
                String input = baos.toString();
                Map<String, String> res = parseResponse(input);
                return res.get("Auth");
        }
        return null;
    }

    /**
     * Parses a response string into a usable data map.
     *
     * @param response The response from Google
     * @return A Map representation.
     */
    private static Map<String, String> parseResponse(String response) {
        Map<String, String> map = new HashMap<String, String>();
        if (response != null) {
            String[] lines = response.split("\n");
            for (String line : lines) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    map.put(parts[0], parts[1]);
                }
            }
        }
        return map;
    }

    private static String UTF8 = "UTF-8";


    /**
     * Send message to mobile device.
     *
     * @param cl Google API auth code.
     */
    public static void sendMessage(String cl) throws IOException {
        String key = "invalid";

        StringBuilder postDataBuilder = new StringBuilder();
        postDataBuilder.append("registration_id").append("=").append(key);
        postDataBuilder.append("&").append("collapse_key").append("=").append("0");
        postDataBuilder.append("&").append("data.payload").append("=").append(URLEncoder.encode("test-content", UTF8));
        byte[] postData = postDataBuilder.toString().getBytes(UTF8);

        URL url = new URL("https://android.apis.google.com/c2dm/send");

        HostnameVerifier hVerifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession
                    session) {
                return true;
            }
        };

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(hVerifier);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
        conn.setRequestProperty("Authorization", "GoogleLogin auth="+cl);

        //------------------
        OutputStream out = conn.getOutputStream();
        //--------------------------
        out.write(postData);
        out.close();

        int sw = conn.getResponseCode();
        System.out.println("" + sw);
        switch (sw) {
            case 200:
                System.out.println("Success, but check for errors in the body");
                break;
            case 503:
                System.out.println("Service unavailable");
                break;
            case 401:
                System.out.println(" Invalid authentication token");
                break;
        }

    }
}

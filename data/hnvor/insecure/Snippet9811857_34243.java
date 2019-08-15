public class C2DMTest 
{
    public static void main(String... args) throws Exception 
    {
        //authentcation using email1
        String auth = "DQAAALoAAADu5rxXlYkYecVYIYKywIkvhhb70SMXgniYqeCNt15i70aO6wmuPBrAYZMMort0z1InHsjblB_R9sLR0wa6mD1CEM9IZH_POhC9iGLKsBO9EBremJJzTCzmj9EVCCWv3-SxYAOJ1CCKVNE7x9oHAehTSapBYWwk80ZjQ19rC2Q8BRctF3aPDLbZBbjyq6rQjUbEQU1vA_WTbgwyENSmNOT_GtTaEUCYtmKauRoOW6TV-LxTWTsvzUApCclSqovcXlk";
        sendMessage(auth);
    }

    /**
     * Send message to mobile device.
     */
    @SuppressWarnings("deprecation")
    public static void sendMessage(String cl) throws IOException 
    {

        String key = "APA91bGgIm0_yjPvr_Wao20XRMJP2HhQSjY2FL9O56CwL73UHqPg_TUrux_voUy3pi_k1aKt1MUt0SkpKCANpC_iw1LQ0szkW9Bs-TFv5lBNyvZZgCBW3H9oIbpQLkz0bUspIY4KqHjaDnaIqSelQLbEbolLQPwHscIQDRIul_W9YVNUTiOhDr8";

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
        switch (sw) 
        {
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


        String responseLine = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
        String[] responseParts = responseLine.split("=", 2);

        if (responseParts[0].equals("id")) 
        {
            System.out.println("Successfully sent data message to device: "+ responseLine);
        }

        if (responseParts[0].equals("Error")) 
        {
            String err = responseParts[1];
            System.out.println("Got error response from Google datamessaging endpoint: "+ err);
            throw new IOException(err);
        }




    }
}

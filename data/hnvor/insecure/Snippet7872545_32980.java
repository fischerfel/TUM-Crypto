public class URLCaller {

private static final String PASSWORD = "xxxxxxx"; //i remove it

private static final String EMAIL = "xxxxxxxxxx@gmail.com"; //i remove it

public static final String PARAM_REGISTRATION_ID = "registration_id";

public static final String PARAM_DELAY_WHILE_IDLE = "delay_while_idle";

public static final String PARAM_COLLAPSE_KEY = "collapse_key";

static String auth_key = null;

static byte[] postData = null;

public static void callGoogle(CalEvent calEvent) throws IOException, SystemException {

    URL url = new URL("https://android.clients.google.com/c2dm/send");

    StringBuilder builder = new StringBuilder();

    HttpsURLConnection conn = null;
    // For each smartPhone
    for(SmartPhone smartPhone : SmartPhoneLocalServiceUtil.getSmartPhones(0, SmartPhoneLocalServiceUtil.getSmartPhonesCount())) {
        auth_key = getAuthorization();
        //Setup data
        System.out.println("" + smartPhone.getRegistrationId());
        builder.append("registration_id=" + smartPhone.getRegistrationId());

        builder.append("&").append(PARAM_COLLAPSE_KEY).append("=").append("0");
        builder.append("&").append("data.payload").append("=").append(URLEncoder.encode("calEventId=" + calEvent.getCalEventId(), "UTF-8"));
        postData = builder.toString().getBytes("UTF-8");

        //Calling server
        conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(new CustomizedHostnameVerifier());
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        conn.setRequestProperty("Content_Lenght", Integer.toString(postData.length));
        conn.setRequestProperty("Authorization", "GoogleLogin auth=" + auth_key);

        // Issue the HTTP POST request
        OutputStream out = conn.getOutputStream();
        out.write(postData);
        out.close();

        System.out.println("Google server said: " + conn.getResponseCode() + ", " + conn.getResponseMessage());

        // Check for updated token header
        String updatedAuthToken = conn.getHeaderField("Update-Client-Auth");
        if(updatedAuthToken != null && !auth_key.equals(updatedAuthToken)) {

            System.out.println("C2DM," + "Got updated auth token from datamessaging servers: " + updatedAuthToken);

            //auth_key = updatedAuthToken;

            String responseLine = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();

            // NOTE: You *MUST* use exponential backoff if you receive a 503 response code.
            // Since App Engine's task queue mechanism automatically does this for tasks that
            // return non-success error codes, this is not explicitly implemented here.
            // If we weren't using App Engine, we'd need to manually implement this.
            if(responseLine == null || responseLine.equals("")) {
                System.out.println("Got " + conn.getResponseCode() + " response from Google AC2DM endpoint.");
                throw new IOException("Got empty response from Google AC2DM endpoint.");
            }

            String[] responseParts = responseLine.split("=", 2);
            if(responseParts.length != 2) {
                System.out.println("Invalid message from google: " + conn.getResponseCode() + " " + responseLine);
                throw new IOException("Invalid response from Google " + conn.getResponseCode() + " " + responseLine);
            }

            if(responseParts[0].equals("id")) {
                System.out.println("Successfully sent data message to device: " + responseLine);
            }

            if(responseParts[0].equals("Error")) {
                String err = responseParts[1];
                System.out.println("Got error response from Google datamessaging endpoint: " + err);
                // No retry.
                //throw new IOException(err);
            }
        }

    }

}

public static String getAuthorization() {
    String authToken = null;

    HttpURLConnection urlConnection;
    URL url;
    try {
        url = new URL("https://www.google.com/accounts/ClientLogin");

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        StringBuilder builder = new StringBuilder();
        builder.append("Email=").append(EMAIL);
        builder.append("&Passwd=").append(PASSWORD);
        builder.append("&accountType=GOOGLE");
        builder.append("&source=Google-C2DM-Example");
        builder.append("&service=ac2dm");

        OutputStream outputStream = urlConnection.getOutputStream();
        outputStream.write(builder.toString().getBytes("UTF-8"));
        outputStream.close();
        int responseCode = urlConnection.getResponseCode();
        System.out.println(responseCode + "\tSuccess");
        StringBuffer resp = new StringBuffer();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                if(line.startsWith("Auth=")) {
                    resp.append(line.substring(5));
                }
            }
            rd.close();
        }

        authToken = resp.toString();
        System.out.println("AuthTOken" + authToken);
    } catch(MalformedURLException e) {
        e.printStackTrace();
    } catch(ProtocolException e) {
        e.printStackTrace();
    } catch(IOException e) {
        e.printStackTrace();
    }
    return authToken;

}

private static class CustomizedHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import com.liferay.portal.kernel.exception.SystemException;

import fr.intuitiv.dal.model.SmartPhone;
import fr.intuitiv.dal.service.SmartPhoneLocalServiceUtil;

public class URLCaller {

public static void callGoogle() throws IOException, SystemException {
    URL url = new URL("https://android.clients.google.com/c2dm/send");
    StringBuilder builder = new StringBuilder();

    byte[] postData = null;
    HttpsURLConnection conn = null;

    String authorized_Key = getAuthorization();

    // For each smartPhone
    for(SmartPhone smartPhone : SmartPhoneLocalServiceUtil.getSmartPhones(0, SmartPhoneLocalServiceUtil.getSmartPhonesCount())) {

        //Setup data
        builder.append("registration_id=" + smartPhone.getRegistrationId());
        builder.append("&collapse_key=").append("0");
        builder.append("&data.payload=").append("The test work, drink  a beer");
        postData = builder.toString().getBytes("UTF-8");

        //Calling server
        conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(new CustomizedHostnameVerifier());

        conn.setDoOutput(true);
        conn.setUseCaches(false);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        conn.setRequestProperty("Content_Lenght", Integer.toString(postData.length));
        conn.setRequestProperty("Authorization", "GoogleLogin auth=" + authorized_Key);

        // Issue the HTTP POST request
        System.out.println("" + conn.getOutputStream());
        OutputStream out = conn.getOutputStream();
        out.write(postData);
        out.flush();
        System.out.println("Google server said: " + conn.getResponseCode() + ", " + conn.getResponseMessage());

        out.close();
    }
}

public static String getAuthorization() throws IOException {
    // Create the post data
    // Requires a field with the email and the password
    StringBuilder builder = new StringBuilder();
    builder.append("Email=").append(user.config.EMAIL);
    builder.append("&Passwd=").append(user.config.PASSWORD);
    builder.append("&accountType=GOOGLE");
    builder.append("&source=Google-C2DM-Example");
    builder.append("&service=ac2dm");// Setup the Http Post
    byte[] data = builder.toString().getBytes();
    URL url = new URL("https://www.google.com/accounts/ClientLogin");

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setUseCaches(false);
    conn.setDoOutput(true);
    conn.setDoInput(true);

    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setRequestProperty("Content-Length", Integer.toString(data.length));

    // Issue the HTTP POST request
    OutputStream output = conn.getOutputStream();
    output.write(data);
    output.flush();
    // Read the response
    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String[] split = reader.readLine().split("=");
    // Finally get the authentication token
    String clientAuthToken = split[1];
    // To something useful with it

    output.close();

    return clientAuthToken;
}

private static class CustomizedHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

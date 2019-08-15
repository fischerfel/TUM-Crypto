import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InstagramPostHelper {

    private static InstagramPostHelper instance = null;

    protected InstagramPostHelper() {}

    public static InstagramPostHelper getInstance() {
        if (instance == null) {
            instance = new InstagramPostHelper();
        }
        return instance;
    }

    private String GenerateSignature(String value, String key) 
    {
        String result = null;
        try {
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(value.getBytes());
            byte[] hexBytes = new Hex().encode(rawHmac);
            result = new String(hexBytes, "UTF-8");
    }
    catch (Exception e) {

    }
        return result;

    }

    private static final String COOKIES_HEADER = "Set-Cookie";
    public static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    private HttpsURLConnection conn;

    private static String TextUtilsJoin(CharSequence delimiter, List<HttpCookie> list) {
            StringBuilder sb = new StringBuilder();
            boolean firstTime = true;
            for (Object token: list) {

                if (token.toString().trim().length()<1) continue;

                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(delimiter + " ");
                }

                sb.append(token);

            }
            return sb.toString();
        }


    private String GetJSONStringAndPostData(String jsonaddress,String postdata,String agent)
    {
        String sonuc = "";
        try {

            byte[] postDataBytes = postdata.toString().getBytes("UTF-8");


            URL url = new URL(jsonaddress);


            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setRequestProperty("User-Agent", agent);

            //Set Cookies
            if(msCookieManager.getCookieStore().getCookies().size() > 0)
            {
              conn.setRequestProperty("Cookie", TextUtilsJoin(";",  msCookieManager.getCookieStore().getCookies()));    
            }

            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            //Get Cookies
            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
            if(cookiesHeader != null)
            {
                for (String cookie : cookiesHeader) 
                {
                  msCookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
                }               
            }



            BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                sonuc += output;
            }
            conn.disconnect();
          } catch (MalformedURLException e) {
            return "";
          } catch (IOException e) {
            return "";
          }
        return sonuc;
    }

    public void SendImage(String Caption,byte[] ImageByteArray) throws UnsupportedEncodingException, ParseException
    {
        String Agent = "Instagram 6.21.2 Android (19/4.4.2; 480dpi; 1152x1920; Meizu; MX4; mx4; mt6595; en_US)";
        String Guid = java.util.UUID.randomUUID().toString();
        String DeviceId = "android-" + Guid;
        String Data = "{\"device_id\":\""  +  DeviceId + "\",\"guid\":\"" + Guid + "\",\"username\":\"MYUSERNAME\",\"password\":\"MYPASSWORD\",\"Content-Type\":\"application/x-www-form-urlencoded; charset=UTF-8\"}"; 
        String Sig = GenerateSignature(Data, "25eace5393646842f0d0c3fb2ac7d3cfa15c052436ee86b5406a8433f54d24a5");
        Data  = "signed_body=" + Sig + "." +  URLEncoder.encode(Data, "UTF-8") + "&ig_sig_key_version=4";

        if (msCookieManager.getCookieStore()!= null)
        {
            msCookieManager.getCookieStore().removeAll();
        }
        //Login Request
        String login = GetJSONStringAndPostData("https://instagram.com/api/v1/accounts/login/", Data,Agent);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(login);
        JSONObject jsonObject = (JSONObject) obj;
        if (((String) jsonObject.get("status")).equals("ok")) //Login SuccessFul
        {
            //Login Successful
            System.out.println("Login Successful !");

           //Post Image
            String upload = "";

            try {
                final HttpMultipartHelper http = new HttpMultipartHelper(new URL("https://instagram.com/api/v1/media/upload/"));
                http.addFormField("device_timestamp", Long.toString((new Date()).getTime()));
                http.addFilePart("photo", ImageByteArray);
                final byte[] bytes = http.finish();
                upload = new String(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }


            System.out.println(upload);
            obj = parser.parse(upload);
            jsonObject = (JSONObject) obj;
            if (((String) jsonObject.get("status")).equals("ok")) //Login SuccessFul
            {
                String mediaID = (String) jsonObject.get("media_id");

                Data = "{\"device_id\":\"" + DeviceId + "\",\"guid\":\"" + Guid + "\",\"media_id\":\"" + mediaID + "\",\"caption\":\"" + Caption + "\",\"device_timestamp\":\"" + Long.toString((new Date()).getTime()).substring(0,10) + "\",\"source_type\":\"5\",\"filter_type\":\"0\",\"extra\":\"{}\",\"Content-Type\":\"application/x-www-form-urlencoded; charset=UTF-8\"}";
                Sig = GenerateSignature(Data, "25eace5393646842f0d0c3fb2ac7d3cfa15c052436ee86b5406a8433f54d24a5");

                Data  = "signed_body=" + Sig + "." +  URLEncoder.encode(Data, "UTF-8") + "&ig_sig_key_version=6";

                //Login Request
                System.out.println(Data);
                String result = GetJSONStringAndPostData("https://instagram.com/api/v1/media/configure/", Data,Agent);

                System.out.println(result);
            }


        }
        else //Login UnsuccessFul
        {
            System.out.println("Login Unsuccessful !" + login);
        }


    }

}

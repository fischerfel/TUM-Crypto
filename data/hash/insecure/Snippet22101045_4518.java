import java.math.BigInteger;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Api {
    private static String api_url = "http://myurl.co.uk/api";
    private static VenueManager venueManager;
    private static final String BASE_URL = "http://myurl.co.uk/api";
    private static boolean success = false;
    private static AsyncHttpClient client = null;
    public static final String api_key = "API_KEY";
    public static final String api_secret = "API_SECRET";
    static final int DEFAULT_TIMEOUT = 20 * 1000;

//  public static AsyncHttpClient get_client()
//  {
//      if(client != null)
//      {
//          return client;
//      }
//      else
//      {
//          client = new AsyncHttpClient();
//          try 
//          {
//              KeyStore trustStore = //KeyStore.getInstance(KeyStore.getDefaultType());
//              trustStore.load(null, null);
//              SocketFactory sf = new SocketFactory(trustStore);
//              //sf.setHostnameVerifier(SocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//              client.setSSLSocketFactory(sf);
//              client.setTimeout(DEFAULT_TIMEOUT);
//          }
//          catch (Exception e) 
//          { 
//              Log.v("API", e.toString());
//          }
//          
//          return client;
//      }
//  }

    public static String get_api_url()
    {
        return api_url;
    }

    public static String search_venues(double lat, double lng)
    {
        return get_api_url() + "/venues?lat=" + lat + "&lng=" + lng + "&radius=1.0";
    }

    public static String post_review()
    {
        return get_api_url() + "/review";
    }

    private static String getAbsoluteUrl(String relativeUrl) {
         return BASE_URL + relativeUrl;
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(5, DEFAULT_TIMEOUT);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(5, DEFAULT_TIMEOUT);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static String sha1(String s) {
        MessageDigest digest = null;
        try 
        {
            digest = MessageDigest.getInstance("SHA-1");
        } 
        catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }

        digest.reset();
        byte[] data = digest.digest(s.getBytes());
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }
}

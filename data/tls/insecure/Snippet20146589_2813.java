import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class TwitterConnection {

    public static void main(String args[]) throws UnsupportedEncodingException {

        // System.out.println(System.getProperty("java.classpath"));

        String oauth_signature_method = "HMAC-SHA1";

        String oauth_consumer_key = "MlyUxiF1ihXIymYIPWZCA";

        String uuid_string = UUID.randomUUID().toString();

        uuid_string = uuid_string.replaceAll("-", "");

        String oauth_nonce = uuid_string; // any relatively random alphanumeric
                                            // string will work here. I used
                                            // UUID minus "-" signs

        long timestamp_at_entry = new Date().getTime();
        String oauth_timestamp = (new Long(timestamp_at_entry / 1000))
                .toString(); // get current time in milliseconds, then divide by
                                // 1000 to get seconds

        // I'm not using a callback value. Otherwise, you'd need to include it
        // in the parameter string like the example above

        // the parameter string must be in alphabetical order

        String parameter_string = "oauth_consumer_key=" + oauth_consumer_key
                + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method="
                + oauth_signature_method + "&oauth_timestamp="
                + oauth_timestamp + "&oauth_version=1.0";

        System.out.println("parameter_string=" + parameter_string);

        String signature_base_string = "POST&https%3A%2F%2Fapi.twitter.com%2Foauth%2Frequest_token&"
                + URLEncoder.encode(parameter_string, "UTF-8");

        System.out.println("signature_base_string=" + signature_base_string);

        String oauth_signature = "";

        try {

            oauth_signature = computeSignature(signature_base_string,
                    "122595245-sPcfsJO3GRkJpjzgFLZ918OaHkxeSmwYg9WDn23Z"); // note
                                                                            // the
                                                                            // &
                                                                            // at
                                                                            // the
                                                                            // end.
                                                                            // Normally
                                                                            // the
                                                                            // user
                                                                            // access_token
                                                                            // would
                                                                            // go
                                                                            // here,
                                                                            // but
                                                                            // we
                                                                            // don't
                                                                            // know
                                                                            // it
                                                                            // yet
                                                                            // for
                                                                            // request_token

            System.out.println("oauth_signature="
                    + URLEncoder.encode(oauth_signature, "UTF-8"));

        } catch (GeneralSecurityException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        String authorization_header_string = "OAuth oauth_consumer_key=\""
                + oauth_consumer_key
                + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" +

                oauth_timestamp + "\",oauth_nonce=\"" + oauth_nonce
                + "\",oauth_version=\"1.0\",oauth_signature=\""
                + URLEncoder.encode(oauth_signature, "UTF-8") + "\"";

        System.out.println("authorization_header_string="
                + authorization_header_string);

        String oauth_token = "";

        HttpClient httpclient = new DefaultHttpClient();
        try {

//          HttpParams params = new SyncBasicHttpParams();
//          HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//          HttpProtocolParams.setContentCharset(params, "UTF-8");
//          HttpProtocolParams.setUserAgent(params, "HttpCore/1.1");
//          HttpProtocolParams.setUseExpectContinue(params, false);
//
//          HttpProcessor httpproc = new ImmutableHttpProcessor(
//                  new HttpRequestInterceptor[] {
//                          // Required protocol interceptors
//                          new RequestContent(), new RequestTargetHost(),
//                          // Recommended protocol interceptors
//                          new RequestConnControl(), new RequestUserAgent(),
//                          new RequestExpectContinue() });
//
//          HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
//          HttpContext context = new BasicHttpContext(null);
//          HttpHost host = new HttpHost(
//                  "https://api.twitter.com/oauth/request_token", 443); // use
//                                                                          // 80
//                                                                          // if
//                                                                          // you
//                                                                          // want
//                                                                          // regular
//                                                                          // HTTP
//                                                                          // (not
//                                                                          // HTTPS)
//          DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
//
//          context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
//          context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);
//
//          // initialize the HTTPS connection
//          SSLContext sslcontext = SSLContext.getInstance("TLS");
//          sslcontext.init(null, null, null);
//          SSLSocketFactory ssf = sslcontext.getSocketFactory();

            // for HTTP, use this instead of the above.
            // Socket socket = new Socket(host.getHostName(), host.getPort());
            // conn.bind(socket, params);

//          BasicHttpEntityEnclosingRequest request2 = new BasicHttpEntityEnclosingRequest(
//                  "POST", "https://api.twitter.com/oauth/request_token");
//          request2.setEntity(new StringEntity("",
//                  "application/x-www-form-urlencoded", "UTF-8"));
//          request2.setParams(params);
//          request2.addHeader("Authorization", authorization_header_string); // this
//                                                                              // is
//                                                                              // where
//                                                                              // we're
//                                                                              // adding
//                                                                              // that
//                                                                              // required
//                                                                              // "Authorization: BLAH"
//                                                                              // header.
//          httpexecutor.preProcess(request2, httpproc, context);
//          HttpResponse response2 = httpexecutor.execute(request2, conn,
//                  context);
//          //
             HttpPost httppost = new
             HttpPost("https://api.twitter.com/oauth/request_token");

             httppost.setHeader("Authorization",authorization_header_string);


             ResponseHandler<String> responseHandler = new
             BasicResponseHandler();

             String responseBody = httpclient.execute(httppost,
             responseHandler);

             oauth_token =
             responseBody.substring(responseBody.indexOf("oauth_token=") + 12,
             responseBody.indexOf("&oauth_token_secret="));

             System.out.println(responseBody);

        }

        catch (ClientProtocolException cpe) {
            System.out.println(cpe.getMessage());
        }

        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    private static String computeSignature(String baseString, String keyString)
            throws GeneralSecurityException, UnsupportedEncodingException {

        SecretKey secretKey = null;

        byte[] keyBytes = keyString.getBytes();

        secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");

        mac.init(secretKey);

        byte[] text = baseString.getBytes();

        return new String(Base64.encodeBase64(mac.doFinal(text))).trim();

    }

}

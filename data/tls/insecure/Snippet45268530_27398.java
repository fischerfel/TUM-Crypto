package main.java.com.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jettison.json.JSONObject;



   public class Token {

    private final String USER_AGENT = "Mozilla/5.0";

    public String Token() throws Exception
    {
        Token http = new Token();

        http.sendGet();

        String token = http.sendPost();

        return token;
    }

    private String sendPost() throws  Exception
    {

        String url = "https://YOUR_AUTH0_DOMAIN/oauth/token";

        HttpClient client = new DefaultHttpClient();
        HttpClient httpClient1 = wrapClient(client);
        HttpPost post = new HttpPost(url);

        post.setHeader("User-Agent" , "Mozilla/5.0");

        List urlParam =  (List) new ArrayList();
        urlParam.add(new BasicNameValuePair("client_id", ""));
        urlParam.add(new BasicNameValuePair("grant_type", ""));
        urlParam.add(new BasicNameValuePair("client_secret", ""));

       post.setEntity(new UrlEncodedFormEntity(urlParam));

        HttpResponse response = httpClient1.execute(post);

        BufferedReader rd = new BufferedReader(new 
        InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";

        while ((line = rd.readLine()) != null){
            result.append(line);
        }

        String[] JsonTags0 = result.toString().split(",");
        String[] JsonTags1 = result.toString().split(":");
        String token1 = JsonTags1[1].trim();

        return token1.substring(1,37);

    }

    private void sendGet()
    {

    }

    public static HttpClient wrapClient(HttpClient base)
    {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager1();


            ctx.init(null, new TrustManager[] {tm},null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ClientConnectionManager ccm = base.getConnectionManager(ctx , SSLSocketFactory , ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf , 443));
            return new DefaultHttpClient(ccm, base.getParams());

        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}

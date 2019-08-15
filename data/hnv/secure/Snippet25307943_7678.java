package using_net;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import logger.AndroidLogger;
import other.GetException;
import other.PostException;

/**
 * Created by ehog on 2013.12.11..
 */
public class BaseAsyncTask<Paramets, Progress,Result> extends AsyncTask<Paramets,Progress,Result>
{
    protected Context _context;

    public BaseAsyncTask(Context context)
    {
        _context = context;
    }

    protected String Get(String url) throws GetException
    {
        try
        {
            AndroidLogger.PushDebugMessage("Web access: "+url);
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            for(String line = null; (line = reader.readLine()) != null;) {
                builder.append(line);
            }
            return builder.toString();
        }
        catch (Exception e) {
            throw new GetException(e);
        }
    }

    protected String Post(String url, String message_json) throws PostException
    {
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        DefaultHttpClient client = new DefaultHttpClient();
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        HttpClient httpclient = new DefaultHttpClient(mgr, client.getParams());
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

        HttpPost httpost = new HttpPost(url);
        try
        {
            List<NameValuePair> nvp = new ArrayList<NameValuePair>(1);
            nvp.add(new BasicNameValuePair("json_data",message_json));
            httpost.setHeader("User-Agent", "Your.browser.header");
            httpost.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8));
            httpost.addHeader("Accept-Encoding", "gzip");
            HttpResponse response = httpclient.execute(httpost);

            int code = response.getStatusLine().getStatusCode();

            if(code < 200 || code >= 300) {
                networkError(code);
                return null;
            }
            InputStream instream = response.getEntity().getContent();
            Header contentEncoding = response.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip"))
            {
                instream = new GZIPInputStream(instream);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for(String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        } catch (Exception x) {
            throw new PostException(x);
        }
    }

    protected void networkError(int code) {

    }

    protected void process(Result result) {

    }

    protected void error() {

    }

    @Override
    protected Result doInBackground(Paramets... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Result result) {
        if(result == null) error();
        else process(result);
    }
}

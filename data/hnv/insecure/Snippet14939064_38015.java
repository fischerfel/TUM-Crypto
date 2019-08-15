import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import android.widget.Toast;

public class MainActivity extends Activity {

    private InputStream is;
    private StringBuilder sb;
    private String result;
    private JSONArray data;
    private JSONObject json_data11;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream is = null;
        String result = "";
        JSONObject jArray = null;

        // http post
        try {

            HttpClient httpclient = getNewHttpClient();
            final String body = String
                    .format("{\"block_face_line\" : [{\"longitude\" : -71.34345555,\"latitude\" : 42.7794343 },{\"longitude\" : -71.4473179666667,\"latitude\" : 42.7336227666667  },  {\"longitude\" : -71.4461721166667,\"latitude\" : 42.7321493333333  },{\"longitude\" : -71.4473179662267,\"latitude\" : 42.726227666667  } ],\"block_face_curb_side\" : \"LEFT\",\"block_face_collector_id\" : \"3\"}");
            HttpPost httppost = new HttpPost(
                    "https://parkme.goldenware.com/cgi-bin/db.test?COMMAND=add&TABLE=block_faces&USER=1&SUBSYSTEM=walker");
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type",
                "application/json; charset=utf-8");

        httppost.setHeader("Content-length",
                Integer.toString(body.length()));

            httppost.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials("parkme", "parkme"),
                    "UTF-8", false));

            httppost.setEntity(new StringEntity(body, "utf-8"));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object

        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                .show();
        Log.e("msg", result);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
